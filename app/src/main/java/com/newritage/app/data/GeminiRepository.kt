package com.newritage.app.data

import android.util.Log
import com.newritage.app.network.GeminiApi
import com.newritage.app.network.PromptBuilder
import com.newritage.app.ui.main.knot.model.KnotInfo
import com.newritage.app.ui.main.knot.model.KnotRepository
import com.newritage.app.ui.main.knot.recommend.DiaryEntry
import com.newritage.app.ui.main.knot.recommend.RecommendationEngine
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

/** 매듭 추천 API 응답 — AI가 고른 매듭과 그 추천 이유. */
data class KnotRecommendation(val knot: KnotInfo, val reason: String)

/** [resolveMonthlyKnot]의 결과 — 렌더링에 필요한 매듭 종류·이름·의미와, 있다면 AI 추천 이유. */
data class ResolvedKnot(
    val knotType: KnotType,
    val name: String,
    val meaning: String,
    /** null이면 AI 추천 이유가 없음(일기가 없어 로컬 폴백으로 정해졌거나 API가 끝내 실패한 경우). */
    val reason: String?
)

/**
 * Gemini API(gemini-3.5-flash) 연동 저장소.
 *
 * AI(GeminiApi)는 DB에 직접 접근하지 않는다 — 이 Repository가 [sessionDao]로 필요한 데이터를 조회하고
 * [PromptBuilder]로 프롬프트를 구성한 뒤 [GeminiApi]를 호출한다. API 호출이 실패하면(키 누락, 네트워크 오류,
 * 응답 파싱 실패 등) null을 반환하므로, 호출자는 항상 API를 우선 시도하고 null일 때만 기존 로컬 로직으로
 * 폴백해야 한다.
 */
class GeminiRepository(private val sessionDao: SessionDao) {

    companion object {
        private const val TAG = "GeminiRepository"

        /** 압력이 이 값(kPa)을 넘으면 '이탈'로 간주한다 — MeasurementActivity의 이탈 카운트 기준과 동일. */
        private const val STABLE_THRESHOLD = 50f

        /** recommendKnot()이 실패(네트워크 오류/형식 불일치)하면 이 횟수만큼 다시 시도한다. */
        private const val RECOMMEND_KNOT_ATTEMPTS = 2
    }

    /**
     * 명상 종료 직후 오늘의 세션 피드백 생성. 실패 시 null.
     * @param stableRatio 이번 세션의 안정 상태 비율(0~100).
     * @param readings 이번 세션의 원시 센서 데이터(시간순). 초반/중반/후반 압력 흐름 분석에 쓰인다.
     */
    suspend fun generateDailyFeedback(
        session: Session,
        stableRatio: Float,
        readings: List<SensorReading> = emptyList()
    ): String? = runCatching {
        val (system, user) = PromptBuilder.buildDailyFeedbackPrompt(session, stableRatio, readings)
        GeminiApi.chatCompletion(system, user)
    }.onFailure { Log.e(TAG, "일간 AI 피드백 생성 실패", it) }.getOrNull()

    /**
     * 하루 압력 변화 그래프 이미지([chartImageBase64], PNG를 base64로 인코딩한 값)를 첨부해
     * 오늘의 전반적인 명상 추세를 분석한 문구를 생성한다. 실패 시 null.
     */
    suspend fun generateDailyTrendFeedback(session: Session, chartImageBase64: String): String? = runCatching {
        val (system, user) = PromptBuilder.buildDailyTrendPrompt(session)
        GeminiApi.chatCompletion(system, user, imageBase64 = chartImageBase64)
    }.onFailure { Log.e(TAG, "일간 추세 이미지 분석 실패", it) }.getOrNull()

    /** [endDate](yyyy-MM-dd, 보통 오늘)로부터 최근 30일 세션을 바탕으로 월간 피드백 생성. 실패 시 null. */
    suspend fun generateMonthlyFeedback(endDate: String): String? {
        val startDate = shiftDate(endDate, -29)
        val sessions = sessionDao.getSessionsInRange(startDate, endDate)
        if (sessions.isEmpty()) return null

        return runCatching {
            val readings = sessionDao.getReadingsInRange(startDate, endDate)
            val stableRatio = stableRatioOf(readings)
            val (system, user) = PromptBuilder.buildMonthlyFeedbackPrompt(sessions, stableRatio)
            GeminiApi.chatCompletion(system, user)
        }.onFailure { Log.e(TAG, "월간 AI 피드백 생성 실패", it) }.getOrNull()
    }

    /**
     * [yearMonth](yyyy-MM)의 매듭을 "한 번만" 정해서 돌려준다. [monthlyKnotDao]에 이미 저장된 값이
     * 있으면 그대로 재사용하고(같은 달을 다시 열어도 매듭 종류가 바뀌지 않는다), 없으면 그 달의
     * 일기를 분석해 새로 정한 뒤 저장한다. 일기가 있으면 Gemini API를 우선 시도하고(재시도 포함),
     * 실패하면 로컬 추천(RecommendationEngine)으로, 일기가 있어도 실을 하나도 안 받았으면 날짜 기반
     * 고정 매핑(KnotType.forDate)으로 정한다. 그 달에 실을 받은 세션이 하나도 없으면 null — 매듭은
     * 실이 있어야 생긴다(일기만으로는 생기지 않는다).
     */
    suspend fun resolveMonthlyKnot(monthlyKnotDao: MonthlyKnotDao, yearMonth: String): ResolvedKnot? {
        monthlyKnotDao.getByYearMonth(yearMonth)?.let { persisted ->
            knotTypeOrNull(persisted.knotTypeName)?.let { knotType ->
                return toResolvedKnot(knotType, persisted.reason.ifBlank { null })
            }
        }

        val monthSessions = sessionDao.getSessionsByMonth(yearMonth)
        val hasThread = monthSessions.any { it.threadColor.isNotBlank() }
        if (!hasThread) return null

        val diarySessions = monthSessions.filter { it.emotion.isNotBlank() }

        val (knotType, reason) = if (diarySessions.isEmpty()) {
            KnotType.forDate("$yearMonth-01") to null
        } else {
            val aiResult = requestKnotRecommendation(diarySessions.map { it.date to it.emotion })
            if (aiResult != null) {
                KnotType.fromRecommendationId(aiResult.knot.id) to aiResult.reason
            } else {
                val localEntries = diarySessions.map { DiaryEntry(LocalDate.parse(it.date), it.emotion) }
                KnotType.fromRecommendationId(RecommendationEngine.recommendKnot(localEntries).id) to null
            }
        }

        monthlyKnotDao.insert(MonthlyKnot(yearMonth, knotType.name, reason ?: ""))
        return toResolvedKnot(knotType, reason)
    }

    /**
     * [yearMonth]에 저장돼 있던 매듭을 지우고 지금 실 데이터를 기준으로 다시 계산한다. 개발자
     * 날짜바로 그 달이 "다시" 닫힐 때(디버깅 중 날짜를 되돌렸다 다시 감았을 때) 쓴다 — 저장된 값을
     * 그대로 재사용하는 [resolveMonthlyKnot]과 달리 항상 새로 계산한다.
     */
    suspend fun reassignMonthlyKnot(monthlyKnotDao: MonthlyKnotDao, yearMonth: String): ResolvedKnot? {
        monthlyKnotDao.deleteByYearMonth(yearMonth)
        return resolveMonthlyKnot(monthlyKnotDao, yearMonth)
    }

    private fun toResolvedKnot(knotType: KnotType, reason: String?): ResolvedKnot {
        val knotInfo = KnotRepository.knots.firstOrNull { it.name == knotType.displayName }
        return ResolvedKnot(
            knotType = knotType,
            name = knotInfo?.name ?: knotType.displayName,
            meaning = knotInfo?.meaning ?: "",
            reason = reason
        )
    }

    private fun knotTypeOrNull(name: String): KnotType? = runCatching { KnotType.valueOf(name) }.getOrNull()

    /**
     * LLM 응답은 매번 문구가 살짝 달라질 수 있어(온도 0.5) 형식이 안 맞아 파싱이 실패하는 경우가
     * 있다 — 한 번 더 재시도해 첫 시도의 우연한 실패로 로컬 폴백까지 가는 것을 줄인다.
     */
    private suspend fun requestKnotRecommendation(entries: List<Pair<String, String>>): KnotRecommendation? {
        val (system, user) = PromptBuilder.buildKnotRecommendationPrompt(entries)
        repeat(RECOMMEND_KNOT_ATTEMPTS) { attempt ->
            val result = runCatching {
                val raw = GeminiApi.chatCompletion(system, user)
                parseKnotRecommendation(raw)
            }.onFailure { Log.e(TAG, "매듭 추천 생성 실패(시도 ${attempt + 1})", it) }.getOrNull()
            if (result != null) return result
        }
        return null
    }

    /**
     * "추천 매듭:\n(이름)\n\n추천 이유:\n(문장)" 형식의 응답을 파싱한다. LLM이 마크다운 강조(**)를
     * 붙이거나 레이블 순서/띄어쓰기를 살짝 바꿔 답하는 경우가 있어 그 정도 편차는 흡수한다.
     * 그래도 이름이 [KnotRepository] 후보 중 하나와 매칭되지 않으면 null(폴백 대상).
     */
    private fun parseKnotRecommendation(raw: String): KnotRecommendation? {
        val cleaned = raw.replace("*", "").replace("#", "")
        val parts = cleaned.split(Regex("추천\\s*이유\\s*[:：]"), limit = 2)
        if (parts.size != 2) return null

        val nameText = parts[0].replace(Regex("추천\\s*매듭\\s*[:：]"), "").trim()
        val reason = parts[1].trim()
        if (nameText.isBlank() || reason.isBlank()) return null

        val normalized = nameText.removeSuffix("매듭").trim()
        val knot = KnotRepository.knots.firstOrNull {
            it.name == nameText || it.name.removeSuffix("매듭") == normalized
        } ?: KnotRepository.knots.firstOrNull { nameText.contains(it.name) }
        ?: return null

        return KnotRecommendation(knot, reason)
    }

    /** [readings] 중 압력([SensorReading.overall])이 [STABLE_THRESHOLD] 이하인 비율(%). 데이터가 없으면 100. */
    private fun stableRatioOf(readings: List<SensorReading>): Float {
        if (readings.isEmpty()) return 100f
        val stableCount = readings.count { it.overall <= STABLE_THRESHOLD }
        return stableCount.toFloat() / readings.size * 100f
    }

    private fun shiftDate(date: String, days: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            time = dateFormat.parse(date) ?: error("날짜 형식이 아닙니다: $date")
            add(Calendar.DAY_OF_MONTH, days)
        }
        return dateFormat.format(calendar.time)
    }
}

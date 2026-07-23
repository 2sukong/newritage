package com.newritage.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 한 번 정해진 "이달의 매듭"을 고정해서 저장한다. 다음 달 1일로 날짜가 넘어가 그 달이 "닫히면"
 * 딱 한 번만 계산돼 저장되고, 그 뒤로는 다시 계산하지 않는다 — Gemini는 같은 입력에도 매번 조금씩
 * 다르게 답할 수 있어(temperature=0.5), 저장 없이 열 때마다 다시 물어보면 매듭 종류가 계속
 * 바뀌어 보인다.
 */
@Entity(tableName = "monthly_knots")
data class MonthlyKnot(
    /** "yyyy-MM" */
    @PrimaryKey val yearMonth: String,
    /** [KnotType.name] (예: "DORAE") */
    val knotTypeName: String,
    /** AI 추천 이유. 일기가 없어 로컬 폴백으로 정해졌으면 빈 문자열. */
    val reason: String
)

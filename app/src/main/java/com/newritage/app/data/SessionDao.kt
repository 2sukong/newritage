package com.newritage.app.data

import androidx.room.*

@Dao
interface SessionDao {

    @Insert
    suspend fun insert(session: Session): Long

    @Insert
    suspend fun insertReadings(readings: List<SensorReading>)

    // 특정 날짜의 가장 최근 세션 하나
    @Query("SELECT * FROM sessions WHERE date = :date ORDER BY id DESC LIMIT 1")
    suspend fun getLatestSessionByDate(date: String): Session?

    // 특정 날짜의 모든 세션 (하루 여러 번 명상 대응)
    @Query("SELECT * FROM sessions WHERE date = :date ORDER BY id ASC")
    suspend fun getSessionsByDate(date: String): List<Session>

    // 세션 하나 단건 조회
    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    suspend fun getSession(sessionId: Long): Session

    // 세션 하나에 속한 원시 데이터
    @Query("SELECT * FROM sensor_readings WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getReadingsForSession(sessionId: Long): List<SensorReading>

    // 특정 날짜에 속한 모든 원시 데이터 (일간 분석용)
    @Query("""
        SELECT sensor_readings.* FROM sensor_readings
        INNER JOIN sessions ON sensor_readings.sessionId = sessions.id
        WHERE sessions.date = :date
        ORDER BY sensor_readings.timestamp ASC
    """)
    suspend fun getReadingsByDate(date: String): List<SensorReading>

    // 날짜 범위의 세션 목록 (주간 분석용) — Fragment가 부르는 이름과 통일
    @Query("SELECT * FROM sessions WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getSessionsInRange(startDate: String, endDate: String): List<Session>

    // 날짜 범위의 원시 데이터 (주간 분석용)
    @Query("""
        SELECT sensor_readings.* FROM sensor_readings
        INNER JOIN sessions ON sensor_readings.sessionId = sessions.id
        WHERE sessions.date BETWEEN :startDate AND :endDate
        ORDER BY sensor_readings.timestamp ASC
    """)
    suspend fun getReadingsInRange(startDate: String, endDate: String): List<SensorReading>

    // 월 패턴의 세션 목록 (월간 분석용)
    @Query("SELECT * FROM sessions WHERE date LIKE :yearMonthPattern ORDER BY date ASC")
    suspend fun getSessionsByMonth(yearMonthPattern: String): List<Session>

    // 월 패턴의 원시 데이터 (월간 분석용)
    @Query("""
        SELECT sensor_readings.* FROM sensor_readings
        INNER JOIN sessions ON sensor_readings.sessionId = sessions.id
        WHERE sessions.date LIKE :yearMonthPattern
        ORDER BY sensor_readings.timestamp ASC
    """)
    suspend fun getReadingsByMonth(yearMonthPattern: String): List<SensorReading>

    // 진동 횟수 업데이트 (필요 시 사용)
    @Query("UPDATE sessions SET vibrationCount = :count WHERE id = :sessionId")
    suspend fun updateVibrationCount(sessionId: Long, count: Int)
}
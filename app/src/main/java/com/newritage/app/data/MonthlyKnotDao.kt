package com.newritage.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MonthlyKnotDao {
    @Query("SELECT * FROM monthly_knots WHERE yearMonth = :yearMonth")
    suspend fun getByYearMonth(yearMonth: String): MonthlyKnot?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(monthlyKnot: MonthlyKnot)

    @Query("DELETE FROM monthly_knots WHERE yearMonth = :yearMonth")
    suspend fun deleteByYearMonth(yearMonth: String)

    /**
     * [yearMonth]보다 나중(문자열 비교로 충분 — "yyyy-MM"은 사전순=시간순)인 모든 매듭을 지운다.
     * 디버깅 날짜를 되돌렸다 다시 감을 때, 예전 타임라인에서 만들어졌던 미래 달의 매듭을 정리하는 데 쓴다.
     */
    @Query("DELETE FROM monthly_knots WHERE yearMonth > :yearMonth")
    suspend fun deleteAfter(yearMonth: String)
}

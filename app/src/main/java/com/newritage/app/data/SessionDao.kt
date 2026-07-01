package com.newritage.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: Session): Long

    @Query("SELECT * FROM sessions WHERE date = :date ORDER BY createdAt ASC")
    suspend fun getSessionsByDate(date: String): List<Session>

    @Query("SELECT COUNT(*) FROM sessions WHERE date = :date")
    suspend fun countSessionsByDate(date: String): Int

    @Query("SELECT * FROM sessions WHERE date = :date AND hasThread = 1 LIMIT 1")
    suspend fun getThreadSessionByDate(date: String): Session?

    @Query("SELECT * FROM sessions WHERE date BETWEEN :start AND :end ORDER BY date ASC, createdAt ASC")
    suspend fun getSessionsInRange(start: String, end: String): List<Session>

    @Query("SELECT * FROM sessions WHERE date LIKE :monthPrefix ORDER BY date ASC, createdAt ASC")
    suspend fun getSessionsByMonth(monthPrefix: String): List<Session>

    @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
    fun getAllSessionsFlow(): Flow<List<Session>>

    @Query("UPDATE sessions SET emotion = :emotion WHERE id = :id")
    suspend fun updateEmotion(id: Long, emotion: String)

    @Query("SELECT DISTINCT date FROM sessions WHERE date LIKE :monthPrefix ORDER BY date ASC")
    suspend fun getDatesWithSessionsByMonth(monthPrefix: String): List<String>

    @Query("SELECT COUNT(DISTINCT date) FROM sessions")
    suspend fun getTotalActiveDays(): Int
}

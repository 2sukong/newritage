package com.newritage.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,              // yyyy-MM-dd
    val sessionIndex: Int = 1,     // 1st, 2nd, ... session of the day
    val hasThread: Boolean = false, // only first session of day gets a thread
    val startTime: String = "",    // HH:mm format
    val endTime: String = "",      // HH:mm format
    val durationSeconds: Int,
    val avgPressure: Float,
    val maxPressure: Float,
    val minPressure: Float,
    val emotion: String = "",
    val threadColor: String = "",      // hex string, empty if no thread
    val threadColorName: String = "",  // Korean name
    val createdAt: Long = System.currentTimeMillis()
)

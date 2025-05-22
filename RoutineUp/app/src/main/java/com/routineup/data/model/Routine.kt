package com.routineup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconResId: Int,
    val time: LocalTime,
    val repeatDays: Set<DayOfWeek>,
    val notificationMinutesBefore: Int = 0,
    val memo: String = "",
    val isCompleted: Boolean = false,
    val completionCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

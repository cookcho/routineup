package com.routineup.data.model

data class AiFeedback(
    val id: Long = 0,
    val title: String,
    val content: String,
    val suggestion: String? = null,
    val weeklyReport: WeeklyReport? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class WeeklyReport(
    val weekStartDate: Long,
    val weekEndDate: Long,
    val completionRate: Float,
    val improvementRate: Float,
    val mostCompletedRoutine: String?,
    val leastCompletedRoutine: String?
)

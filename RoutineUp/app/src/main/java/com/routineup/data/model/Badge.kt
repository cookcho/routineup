package com.routineup.data.model

data class Badge(
    val id: Long = 0,
    val name: String,
    val description: String,
    val iconResId: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

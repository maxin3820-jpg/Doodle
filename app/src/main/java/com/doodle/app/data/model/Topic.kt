package com.doodle.app.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class Topic(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val taskCount: Int = 0  // Number of active tasks in this topic
)

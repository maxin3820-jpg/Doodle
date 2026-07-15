package com.doodle.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

// Data class for topic with task count (used in queries with COUNT)
data class TopicWithCount(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val taskCount: Int
)

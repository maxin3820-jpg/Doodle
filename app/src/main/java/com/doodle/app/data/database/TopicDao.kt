package com.doodle.app.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics ORDER BY createdAt ASC")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity): Long

    @Delete
    suspend fun deleteTopic(topic: TopicEntity)

    @Query("SELECT * FROM tasks WHERE topicId = :topicId AND isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTasksForTopic(topicId: Long): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE topicId = :topicId AND isCompleted = 0")
    suspend fun getActiveTaskCountForTopic(topicId: Long): Int
}

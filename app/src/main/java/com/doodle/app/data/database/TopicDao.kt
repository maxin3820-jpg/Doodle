package com.doodle.app.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Query("SELECT * FROM topics ORDER BY createdAt ASC")
    fun getAllTopics(): Flow<List<TopicEntity>>
    
    @Query("""
        SELECT topics.id, topics.name, topics.createdAt, 
               COUNT(CASE WHEN tasks.isCompleted = 0 THEN 1 END) as taskCount
        FROM topics
        LEFT JOIN tasks ON topics.id = tasks.topicId
        GROUP BY topics.id
        ORDER BY topics.createdAt ASC
    """)
    fun getAllTopicsWithCount(): Flow<List<TopicWithCount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity): Long

    @Delete
    suspend fun deleteTopic(topic: TopicEntity)

    @Query("SELECT * FROM tasks WHERE topicId = :topicId AND isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTasksForTopic(topicId: Long): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE topicId = :topicId AND isCompleted = 0")
    suspend fun getActiveTaskCountForTopic(topicId: Long): Int
}

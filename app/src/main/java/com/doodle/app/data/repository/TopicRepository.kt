package com.doodle.app.data.repository

import com.doodle.app.data.database.TaskDao
import com.doodle.app.data.database.TaskEntity
import com.doodle.app.data.database.TopicDao
import com.doodle.app.data.database.TopicEntity
import com.doodle.app.data.model.Task
import com.doodle.app.data.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicRepository @Inject constructor(
    private val topicDao: TopicDao,
    private val taskDao: TaskDao
) {
    // Combine topics flow with tasks flow to calculate counts reactively
    fun getAllTopics(): Flow<List<Topic>> =
        combine(
            topicDao.getAllTopics(),
            taskDao.getActiveTasks()
        ) { topics, tasks ->
            topics.map { entity ->
                val count = tasks.count { it.topicId == entity.id }
                Topic(
                    id = entity.id,
                    name = entity.name,
                    createdAt = entity.createdAt,
                    taskCount = count
                )
            }
        }

    fun getActiveTasksForTopic(topicId: Long): Flow<List<Task>> =
        topicDao.getActiveTasksForTopic(topicId).map { list -> list.map { it.toTask() } }

    suspend fun addTopic(name: String): Long =
        topicDao.insertTopic(TopicEntity(name = name.trim()))

    suspend fun deleteTopic(topic: Topic) {
        taskDao.deleteTasksByTopicId(topic.id)
        topicDao.deleteTopic(topic.toEntity())
    }

    private fun Topic.toEntity() = TopicEntity(id = id, name = name, createdAt = createdAt)
    private fun TaskEntity.toTask() = Task(
        id = id, title = title, isCompleted = isCompleted,
        createdAt = createdAt, completedAt = completedAt, topicId = topicId
    )
}

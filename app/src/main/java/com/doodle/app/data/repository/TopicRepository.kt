package com.doodle.app.data.repository

import com.doodle.app.data.database.TaskDao
import com.doodle.app.data.database.TaskEntity
import com.doodle.app.data.database.TopicDao
import com.doodle.app.data.database.TopicEntity
import com.doodle.app.data.model.Task
import com.doodle.app.data.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicRepository @Inject constructor(
    private val topicDao: TopicDao,
    private val taskDao: TaskDao          // needed for cascade delete
) {
    fun getAllTopics(): Flow<List<Topic>> =
        topicDao.getAllTopicsWithCount().map { list -> 
            list.map { topicWithCount ->
                Topic(
                    id = topicWithCount.id,
                    name = topicWithCount.name,
                    createdAt = topicWithCount.createdAt,
                    taskCount = topicWithCount.taskCount
                )
            }
        }

    fun getActiveTasksForTopic(topicId: Long): Flow<List<Task>> =
        topicDao.getActiveTasksForTopic(topicId).map { list -> list.map { it.toTask() } }

    suspend fun addTopic(name: String): Long =
        topicDao.insertTopic(TopicEntity(name = name.trim()))

    // Bug 2 fix: delete all tasks belonging to this topic first, then delete the topic
    suspend fun deleteTopic(topic: Topic) {
        taskDao.deleteTasksByTopicId(topic.id)
        topicDao.deleteTopic(topic.toEntity())
    }

    private fun TopicEntity.toTopic(taskCount: Int = 0) = Topic(
        id = id, 
        name = name, 
        createdAt = createdAt,
        taskCount = taskCount
    )
    private fun Topic.toEntity() = TopicEntity(id = id, name = name, createdAt = createdAt)
    private fun TaskEntity.toTask() = Task(
        id = id, title = title, isCompleted = isCompleted,
        createdAt = createdAt, completedAt = completedAt, topicId = topicId
    )
}

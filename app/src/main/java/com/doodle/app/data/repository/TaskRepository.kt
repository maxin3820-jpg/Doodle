package com.doodle.app.data.repository

import com.doodle.app.data.database.TaskDao
import com.doodle.app.data.database.TaskEntity
import com.doodle.app.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getActiveTasks(): Flow<List<Task>> =
        taskDao.getActiveTasks().map { it.map { e -> e.toTask() } }

    fun getCompletedTasks(): Flow<List<Task>> =
        taskDao.getCompletedTasks().map { it.map { e -> e.toTask() } }

    suspend fun addTask(title: String, topicId: Long? = null): Long =
        taskDao.insertTask(TaskEntity(title = title, topicId = topicId))

    suspend fun updateTask(task: Task) = taskDao.updateTask(task.toEntity())

    suspend fun completeTask(task: Task) =
        taskDao.updateTask(task.copy(isCompleted = true, completedAt = System.currentTimeMillis()).toEntity())

    suspend fun uncompleteTask(task: Task) =
        taskDao.updateTask(task.copy(isCompleted = false, completedAt = null).toEntity())

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task.toEntity())

    private fun TaskEntity.toTask() = Task(
        id = id, title = title, isCompleted = isCompleted,
        createdAt = createdAt, completedAt = completedAt, topicId = topicId
    )

    private fun Task.toEntity() = TaskEntity(
        id = id, title = title, isCompleted = isCompleted,
        createdAt = createdAt, completedAt = completedAt, topicId = topicId
    )
}

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
    fun getActiveTasks(): Flow<List<Task>> {
        return taskDao.getActiveTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    fun getCompletedTasks(): Flow<List<Task>> {
        return taskDao.getCompletedTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    suspend fun addTask(title: String): Long {
        val task = TaskEntity(title = title)
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    suspend fun completeTask(task: Task) {
        val updatedTask = task.copy(
            isCompleted = true,
            completedAt = System.currentTimeMillis()
        )
        taskDao.updateTask(updatedTask.toEntity())
    }

    suspend fun uncompleteTask(task: Task) {
        val updatedTask = task.copy(
            isCompleted = false,
            completedAt = null
        )
        taskDao.updateTask(updatedTask.toEntity())
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    private fun TaskEntity.toTask() = Task(
        id = id,
        title = title,
        isCompleted = isCompleted,
        createdAt = createdAt,
        completedAt = completedAt
    )

    private fun Task.toEntity() = TaskEntity(
        id = id,
        title = title,
        isCompleted = isCompleted,
        createdAt = createdAt,
        completedAt = completedAt
    )
}

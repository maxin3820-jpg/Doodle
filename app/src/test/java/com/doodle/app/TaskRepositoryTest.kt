package com.doodle.app

import com.doodle.app.data.database.TaskDao
import com.doodle.app.data.database.TaskEntity
import com.doodle.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class TaskRepositoryTest {

    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        taskDao = mock(TaskDao::class.java)
        repository = TaskRepository(taskDao)
    }

    @Test
    fun `getActiveTasks returns mapped tasks`() = runTest {
        val entities = listOf(
            TaskEntity(1, "Task 1", false, 0L, null),
            TaskEntity(2, "Task 2", false, 0L, null)
        )
        `when`(taskDao.getActiveTasks()).thenReturn(flowOf(entities))

        val tasks = repository.getActiveTasks().first()

        assertEquals(2, tasks.size)
        assertEquals("Task 1", tasks[0].title)
        assertEquals("Task 2", tasks[1].title)
    }

    @Test
    fun `addTask inserts task with correct title`() = runTest {
        val title = "New Task"
        `when`(taskDao.insertTask(any())).thenReturn(1L)

        repository.addTask(title)

        verify(taskDao).insertTask(argThat { it.title == title && !it.isCompleted })
    }

    @Test
    fun `completeTask updates task with completed status`() = runTest {
        val task = com.doodle.app.data.model.Task(1, "Task", false, 0L, null)

        repository.completeTask(task)

        verify(taskDao).updateTask(argThat { it.isCompleted && it.completedAt != null })
    }
}

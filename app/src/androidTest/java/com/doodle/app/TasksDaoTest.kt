package com.doodle.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.doodle.app.data.database.AppDatabase
import com.doodle.app.data.database.TaskDao
import com.doodle.app.data.database.TaskEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetTask() = runTest {
        val task = TaskEntity(title = "Test Task", isCompleted = false)
        val id = taskDao.insertTask(task)

        val tasks = taskDao.getActiveTasks().first()
        assertEquals(1, tasks.size)
        assertEquals("Test Task", tasks[0].title)
    }

    @Test
    fun getActiveTasksExcludesCompleted() = runTest {
        taskDao.insertTask(TaskEntity(title = "Active", isCompleted = false))
        taskDao.insertTask(TaskEntity(title = "Completed", isCompleted = true))

        val activeTasks = taskDao.getActiveTasks().first()
        assertEquals(1, activeTasks.size)
        assertEquals("Active", activeTasks[0].title)
    }

    @Test
    fun getCompletedTasksExcludesActive() = runTest {
        taskDao.insertTask(TaskEntity(title = "Active", isCompleted = false))
        taskDao.insertTask(TaskEntity(title = "Completed", isCompleted = true))

        val completedTasks = taskDao.getCompletedTasks().first()
        assertEquals(1, completedTasks.size)
        assertEquals("Completed", completedTasks[0].title)
    }
}

package com.doodle.app

import app.cash.turbine.test
import com.doodle.app.data.model.Task
import com.doodle.app.data.repository.TaskRepository
import com.doodle.app.ui.viewmodel.TasksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModelTest {

    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TasksViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(TaskRepository::class.java)
        `when`(repository.getActiveTasks()).thenReturn(flowOf(emptyList()))
        viewModel = TasksViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty tasks`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.activeTasks.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `showAddDialog updates state`() = runTest {
        viewModel.showAddDialog()
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.showAddDialog)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTask calls repository with trimmed title`() = runTest {
        val title = "  New Task  "
        
        viewModel.addTask(title)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).addTask("New Task")
    }
}

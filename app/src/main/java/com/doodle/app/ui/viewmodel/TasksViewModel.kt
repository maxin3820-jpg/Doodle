package com.doodle.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.app.data.model.Task
import com.doodle.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.getActiveTasks().collect { tasks ->
                _uiState.update { it.copy(activeTasks = tasks) }
            }
        }
    }

    fun addTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addTask(title.trim())
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            repository.completeTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun showAddDialog() {
        _uiState.update { it.copy(showAddDialog = true) }
    }

    fun hideAddDialog() {
        _uiState.update { it.copy(showAddDialog = false) }
    }

    fun showEditDialog(task: Task) {
        _uiState.update { it.copy(showEditDialog = true, editingTask = task) }
    }

    fun hideEditDialog() {
        _uiState.update { it.copy(showEditDialog = false, editingTask = null) }
    }

    fun showDeleteDialog(task: Task) {
        _uiState.update { it.copy(showDeleteDialog = true, taskToDelete = task) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false, taskToDelete = null) }
    }
}

data class TasksUiState(
    val activeTasks: List<Task> = emptyList(),
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val editingTask: Task? = null,
    val taskToDelete: Task? = null
)

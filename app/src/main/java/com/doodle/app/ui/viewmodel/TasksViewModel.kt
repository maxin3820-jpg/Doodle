package com.doodle.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.app.data.model.Task
import com.doodle.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // Combine DB flow with dialog state in one StateFlow — no manual collect loop
    private val _dialogState = MutableStateFlow(DialogState())

    val uiState: StateFlow<TasksUiState> = combine(
        repository.getActiveTasks(),
        _dialogState
    ) { tasks, dialogs ->
        TasksUiState(
            activeTasks = tasks,
            showAddDialog = dialogs.showAddDialog,
            showEditDialog = dialogs.showEditDialog,
            showDeleteDialog = dialogs.showDeleteDialog,
            editingTask = dialogs.editingTask,
            taskToDelete = dialogs.taskToDelete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TasksUiState()
    )

    fun addTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(title.trim())
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.completeTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun showAddDialog() = _dialogState.update { it.copy(showAddDialog = true) }
    fun hideAddDialog() = _dialogState.update { it.copy(showAddDialog = false) }

    fun showEditDialog(task: Task) = _dialogState.update {
        it.copy(showEditDialog = true, editingTask = task)
    }
    fun hideEditDialog() = _dialogState.update {
        it.copy(showEditDialog = false, editingTask = null)
    }

    fun showDeleteDialog(task: Task) = _dialogState.update {
        it.copy(showDeleteDialog = true, taskToDelete = task)
    }
    fun hideDeleteDialog() = _dialogState.update {
        it.copy(showDeleteDialog = false, taskToDelete = null)
    }
}

// Internal state for dialogs only — keeps DB flow separate
private data class DialogState(
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val editingTask: Task? = null,
    val taskToDelete: Task? = null
)

@Stable
data class TasksUiState(
    val activeTasks: List<Task> = emptyList(),
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val editingTask: Task? = null,
    val taskToDelete: Task? = null
)

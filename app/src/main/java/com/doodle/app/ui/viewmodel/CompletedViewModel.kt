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
class CompletedViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompletedUiState())
    val uiState: StateFlow<CompletedUiState> = _uiState.asStateFlow()

    init {
        loadCompletedTasks()
    }

    private fun loadCompletedTasks() {
        viewModelScope.launch {
            repository.getCompletedTasks().collect { tasks ->
                _uiState.update { it.copy(completedTasks = tasks) }
            }
        }
    }

    fun uncompleteTask(task: Task) {
        viewModelScope.launch {
            repository.uncompleteTask(task)
        }
    }
}

data class CompletedUiState(
    val completedTasks: List<Task> = emptyList()
)

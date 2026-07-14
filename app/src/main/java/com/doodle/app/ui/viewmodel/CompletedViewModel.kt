package com.doodle.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.app.data.model.Task
import com.doodle.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompletedViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val uiState: StateFlow<CompletedUiState> = repository.getCompletedTasks()
        .map { CompletedUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CompletedUiState()
        )

    fun uncompleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uncompleteTask(task)
        }
    }
}

data class CompletedUiState(
    val completedTasks: List<Task> = emptyList()
)

package com.doodle.app.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.app.data.model.Task
import com.doodle.app.data.model.Topic
import com.doodle.app.data.repository.TaskRepository
import com.doodle.app.data.repository.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _dialogState = MutableStateFlow(TopicDialogState())

    val uiState: StateFlow<TopicsUiState> = combine(
        topicRepository.getAllTopics(),
        _dialogState
    ) { topics, dialogs ->
        TopicsUiState(
            topics = topics,
            showAddTopicDialog = dialogs.showAddTopicDialog,
            showAddTaskDialog = dialogs.showAddTaskDialog,
            showDeleteTopicDialog = dialogs.showDeleteTopicDialog,
            showEditTaskDialog = dialogs.showEditTaskDialog,
            showDeleteTaskDialog = dialogs.showDeleteTaskDialog,
            selectedTopic = dialogs.selectedTopic,
            editingTask = dialogs.editingTask,
            taskToDelete = dialogs.taskToDelete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TopicsUiState()
    )

    // Per-topic task flows — keyed by topicId, created lazily
    private val taskFlows = mutableMapOf<Long, StateFlow<List<Task>>>()

    fun getTasksForTopic(topicId: Long): StateFlow<List<Task>> =
        taskFlows.getOrPut(topicId) {
            topicRepository.getActiveTasksForTopic(topicId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        }

    // ── Topic actions ──────────────────────────────────────────────────────
    fun addTopic(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) { topicRepository.addTopic(name) }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch(Dispatchers.IO) { topicRepository.deleteTopic(topic) }
    }

    // ── Task actions ───────────────────────────────────────────────────────
    fun addTaskToTopic(title: String, topicId: Long) {
        if (title.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) { taskRepository.addTask(title.trim(), topicId) }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { taskRepository.completeTask(task) }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { taskRepository.updateTask(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { taskRepository.deleteTask(task) }
    }

    // ── Dialog state ───────────────────────────────────────────────────────
    fun showAddTopicDialog() = _dialogState.update { it.copy(showAddTopicDialog = true) }
    fun hideAddTopicDialog() = _dialogState.update { it.copy(showAddTopicDialog = false) }

    fun showAddTaskDialog(topic: Topic) = _dialogState.update {
        it.copy(showAddTaskDialog = true, selectedTopic = topic)
    }
    fun hideAddTaskDialog() = _dialogState.update {
        it.copy(showAddTaskDialog = false, selectedTopic = null)
    }

    fun showDeleteTopicDialog(topic: Topic) = _dialogState.update {
        it.copy(showDeleteTopicDialog = true, selectedTopic = topic)
    }
    fun hideDeleteTopicDialog() = _dialogState.update {
        it.copy(showDeleteTopicDialog = false, selectedTopic = null)
    }

    fun showEditTaskDialog(task: Task) = _dialogState.update {
        it.copy(showEditTaskDialog = true, editingTask = task)
    }
    fun hideEditTaskDialog() = _dialogState.update {
        it.copy(showEditTaskDialog = false, editingTask = null)
    }

    fun showDeleteTaskDialog(task: Task) = _dialogState.update {
        it.copy(showDeleteTaskDialog = true, taskToDelete = task)
    }
    fun hideDeleteTaskDialog() = _dialogState.update {
        it.copy(showDeleteTaskDialog = false, taskToDelete = null)
    }
}

private data class TopicDialogState(
    val showAddTopicDialog: Boolean = false,
    val showAddTaskDialog: Boolean = false,
    val showDeleteTopicDialog: Boolean = false,
    val showEditTaskDialog: Boolean = false,
    val showDeleteTaskDialog: Boolean = false,
    val selectedTopic: Topic? = null,
    val editingTask: Task? = null,
    val taskToDelete: Task? = null
)

@Immutable
data class TopicsUiState(
    val topics: List<Topic> = emptyList(),
    val showAddTopicDialog: Boolean = false,
    val showAddTaskDialog: Boolean = false,
    val showDeleteTopicDialog: Boolean = false,
    val showEditTaskDialog: Boolean = false,
    val showDeleteTaskDialog: Boolean = false,
    val selectedTopic: Topic? = null,
    val editingTask: Task? = null,
    val taskToDelete: Task? = null
)

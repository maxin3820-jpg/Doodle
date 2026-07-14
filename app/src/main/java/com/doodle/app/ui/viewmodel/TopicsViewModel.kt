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
            selectedTopic = dialogs.selectedTopic
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TopicsUiState()
    )

    // Per-topic task flows — keyed by topicId, created on demand
    private val taskFlows = mutableMapOf<Long, StateFlow<List<Task>>>()

    fun getTasksForTopic(topicId: Long): StateFlow<List<Task>> {
        return taskFlows.getOrPut(topicId) {
            topicRepository.getActiveTasksForTopic(topicId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        }
    }

    fun addTopic(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            topicRepository.addTopic(name)
        }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch(Dispatchers.IO) {
            topicRepository.deleteTopic(topic)
        }
    }

    fun addTaskToTopic(title: String, topicId: Long) {
        if (title.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.addTask(title.trim(), topicId)
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.completeTask(task)
        }
    }

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
}

private data class TopicDialogState(
    val showAddTopicDialog: Boolean = false,
    val showAddTaskDialog: Boolean = false,
    val showDeleteTopicDialog: Boolean = false,
    val selectedTopic: Topic? = null
)

@Immutable
data class TopicsUiState(
    val topics: List<Topic> = emptyList(),
    val showAddTopicDialog: Boolean = false,
    val showAddTaskDialog: Boolean = false,
    val showDeleteTopicDialog: Boolean = false,
    val selectedTopic: Topic? = null
)

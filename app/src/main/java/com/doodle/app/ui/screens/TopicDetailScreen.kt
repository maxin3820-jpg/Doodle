package com.doodle.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doodle.app.R
import com.doodle.app.data.model.Task
import com.doodle.app.ui.components.DeleteConfirmationDialog
import com.doodle.app.ui.components.TaskDialog
import com.doodle.app.ui.components.TaskEditDialog
import com.doodle.app.ui.viewmodel.TopicsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TopicDetailScreen(
    topicId: Long,
    topicName: String,
    onNavigateBack: () -> Unit,
    viewModel: TopicsViewModel = hiltViewModel()
) {
    val tasks by viewModel.getTasksForTopic(topicId).collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isInitialLoad by remember { mutableStateOf(true) }
    
    // Track initial load state
    LaunchedEffect(tasks) {
        if (tasks.isNotEmpty() || !isInitialLoad) {
            isInitialLoad = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = topicName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Find the topic and show add task dialog
                    // If topic not loaded yet, create a temporary Topic object using passed topicId/name
                    val topic = uiState.topics.find { it.id == topicId }
                        ?: com.doodle.app.data.model.Topic(
                            id = topicId,
                            name = topicName,
                            createdAt = System.currentTimeMillis()
                        )
                    viewModel.showAddTaskDialog(topic)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isInitialLoad && tasks.isEmpty()) {
                // Show loading indicator on first load
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (tasks.isEmpty()) {
                // Show empty state after loading
                com.doodle.app.ui.components.EmptyState(
                    title = stringResource(R.string.no_tasks_in_topic_title, topicName),
                    description = stringResource(R.string.tap_plus_to_add),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = tasks,
                        key = { it.id }
                    ) { task ->
                        TopicDetailTaskCard(
                            task = task,
                            onComplete = { viewModel.completeTask(task) },
                            onLongClick = { viewModel.showEditTaskDialog(task) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }

        // Add task dialog
        if (uiState.showAddTaskDialog && uiState.selectedTopic?.id == topicId) {
            TaskDialog(
                title = stringResource(R.string.new_task_in_topic, topicName),
                initialValue = "",
                confirmLabel = stringResource(R.string.add),
                onDismiss = { viewModel.hideAddTaskDialog() },
                onConfirm = { title ->
                    viewModel.addTaskToTopic(title, topicId)
                    viewModel.hideAddTaskDialog()
                }
            )
        }

        // Edit task dialog
        if (uiState.showEditTaskDialog && uiState.editingTask != null) {
            TaskEditDialog(
                task = uiState.editingTask!!,
                onDismiss = { viewModel.hideEditTaskDialog() },
                onEdit = { title ->
                    viewModel.updateTask(uiState.editingTask!!.copy(title = title))
                    viewModel.hideEditTaskDialog()
                },
                onDelete = {
                    viewModel.hideEditTaskDialog()
                    viewModel.showDeleteTaskDialog(uiState.editingTask!!)
                }
            )
        }

        // Delete task dialog
        if (uiState.showDeleteTaskDialog && uiState.taskToDelete != null) {
            DeleteConfirmationDialog(
                onDismiss = { viewModel.hideDeleteTaskDialog() },
                onConfirm = {
                    viewModel.deleteTask(uiState.taskToDelete!!)
                    viewModel.hideDeleteTaskDialog()
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun TopicDetailTaskCard(
    task: Task,
    onComplete: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Swipe right to complete — same pattern as main tasks screen
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd) { 
                onComplete()
                true
            }
            else false
        },
        positionalThreshold = { it * 0.4f }
    )

    // Reset swipe state after task completion animation finishes
    LaunchedEffect(task.id) {
        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = false,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primary
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Complete",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(onClick = {}, onLongClick = onLongClick),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onComplete() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

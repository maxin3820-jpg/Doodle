package com.doodle.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doodle.app.R
import com.doodle.app.data.model.Task
import com.doodle.app.data.model.Topic
import com.doodle.app.ui.components.*
import com.doodle.app.ui.viewmodel.SettingsViewModel
import com.doodle.app.ui.viewmodel.TasksViewModel
import com.doodle.app.ui.viewmodel.TopicsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToTopic: (topicId: Long, topicName: String) -> Unit = { _, _ -> },
    tasksViewModel: TasksViewModel = hiltViewModel(),
    topicsViewModel: TopicsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by tasksViewModel.uiState.collectAsStateWithLifecycle()
    val topicsUiState by topicsViewModel.uiState.collectAsStateWithLifecycle()
    val topicsEnabled by settingsViewModel.topicsEnabled.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            // ── Top app bar + topic pill header in one column ──────────────
            Column {
                TopAppBar(
                    title = { Text(stringResource(R.string.tasks)) },
                    actions = {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = stringResource(R.string.settings)
                            )
                        }
                    }
                )

                // Topic pill header — only shown when topics are enabled
                if (topicsEnabled) {
                    TopicPillHeader(
                        topics = topicsUiState.topics,
                        onTopicClick = { topic ->
                            onNavigateToTopic(topic.id, topic.name)
                        },
                        onTopicLongClick = { topic ->
                            topicsViewModel.showDeleteTopicDialog(topic)
                        },
                        onAddTopic = { topicsViewModel.showAddTopicDialog() }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { tasksViewModel.showAddDialog() },
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
            if (uiState.activeTasks.isEmpty()) {
                EmptyState(
                    title = stringResource(R.string.no_tasks_yet),
                    description = stringResource(R.string.no_tasks_description),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 12.dp, bottom = 88.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.activeTasks,
                        key = { "task_${it.id}" }
                    ) { task ->
                        SwipeToCompleteTaskCard(
                            task = task,
                            onComplete = { tasksViewModel.completeTask(task) },
                            onLongClick = { tasksViewModel.showEditDialog(task) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }

        // ── Dialogs ────────────────────────────────────────────────────────
        if (uiState.showAddDialog) {
            TaskDialog(
                title = stringResource(R.string.new_task),
                initialValue = "",
                onDismiss = { tasksViewModel.hideAddDialog() },
                onConfirm = { title ->
                    tasksViewModel.addTask(title)
                    tasksViewModel.hideAddDialog()
                }
            )
        }

        if (uiState.showEditDialog && uiState.editingTask != null) {
            TaskEditDialog(
                task = uiState.editingTask!!,
                onDismiss = { tasksViewModel.hideEditDialog() },
                onEdit = { title ->
                    tasksViewModel.updateTask(uiState.editingTask!!.copy(title = title))
                    tasksViewModel.hideEditDialog()
                },
                onDelete = {
                    tasksViewModel.hideEditDialog()
                    tasksViewModel.showDeleteDialog(uiState.editingTask!!)
                }
            )
        }

        if (uiState.showDeleteDialog && uiState.taskToDelete != null) {
            DeleteConfirmationDialog(
                onDismiss = { tasksViewModel.hideDeleteDialog() },
                onConfirm = {
                    tasksViewModel.deleteTask(uiState.taskToDelete!!)
                    tasksViewModel.hideDeleteDialog()
                }
            )
        }

        if (topicsUiState.showAddTopicDialog) {
            TaskDialog(
                title = stringResource(R.string.new_topic),
                initialValue = "",
                confirmLabel = stringResource(R.string.add),
                onDismiss = { topicsViewModel.hideAddTopicDialog() },
                onConfirm = { name ->
                    topicsViewModel.addTopic(name)
                    topicsViewModel.hideAddTopicDialog()
                }
            )
        }

        if (topicsUiState.showDeleteTopicDialog && topicsUiState.selectedTopic != null) {
            AlertDialog(
                onDismissRequest = { topicsViewModel.hideDeleteTopicDialog() },
                title = { Text(stringResource(R.string.delete_topic)) },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_topic_message,
                            topicsUiState.selectedTopic!!.name
                        )
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            topicsViewModel.deleteTopic(topicsUiState.selectedTopic!!)
                            topicsViewModel.hideDeleteTopicDialog()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) { Text(stringResource(R.string.delete)) }
                },
                dismissButton = {
                    TextButton(onClick = { topicsViewModel.hideDeleteTopicDialog() }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

// ── Topic pill header — pinned below the top app bar ─────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopicPillHeader(
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
    onTopicLongClick: (Topic) -> Unit,
    onAddTopic: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            topics.forEach { topic ->
                TopicPill(
                    name = topic.name,
                    onClick = { onTopicClick(topic) },
                    onLongClick = { onTopicLongClick(topic) }
                )
            }

            // Add topic pill — always at end
            Surface(
                modifier = Modifier.combinedClickable(onClick = onAddTopic),
                shape = RoundedCornerShape(50),
                color = Color.Transparent,
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text(
                    text = "+ ${stringResource(R.string.new_topic)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopicPill(
    name: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Surface(
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        ),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 0.dp
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
        )
    }
}

// ── Swipe to complete wrapper ──────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SwipeToCompleteTaskCard(
    task: Task,
    onComplete: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd) { onComplete(); true }
            else false
        },
        positionalThreshold = { it * 0.4f }
    )

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
        TaskCard(task = task, onCheckedChange = onComplete, onLongClick = onLongClick)
    }
}

// ── Regular task card ──────────────────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: Task,
    onCheckedChange: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
            Checkbox(checked = task.isCompleted, onCheckedChange = { onCheckedChange() })
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

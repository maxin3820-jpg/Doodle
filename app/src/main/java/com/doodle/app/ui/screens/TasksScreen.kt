package com.doodle.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
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
    tasksViewModel: TasksViewModel = hiltViewModel(),
    topicsViewModel: TopicsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by tasksViewModel.uiState.collectAsStateWithLifecycle()
    val topicsUiState by topicsViewModel.uiState.collectAsStateWithLifecycle()
    val topicsEnabled by settingsViewModel.topicsEnabled.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tasks)) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                    }
                }
            )
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
            val hasGeneralTasks = uiState.activeTasks.isNotEmpty()
            val hasTopics = topicsEnabled && topicsUiState.topics.isNotEmpty()

            if (!hasGeneralTasks && !hasTopics) {
                EmptyState(
                    title = stringResource(R.string.no_tasks_yet),
                    description = stringResource(R.string.no_tasks_description),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ── General tasks ──────────────────────────────────────
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

                    // ── Topics section ─────────────────────────────────────
                    if (topicsEnabled && topicsUiState.topics.isNotEmpty()) {
                        item(key = "topics_header") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                                    .padding(top = if (hasGeneralTasks) 8.dp else 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.topics),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                TextButton(
                                    onClick = { topicsViewModel.showAddTopicDialog() },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.add_topic),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }

                        items(
                            items = topicsUiState.topics,
                            key = { "topic_${it.id}" }
                        ) { topic ->
                            TopicCard(
                                topic = topic,
                                topicsViewModel = topicsViewModel,
                                modifier = Modifier.animateItem()
                            )
                        }
                    } else if (topicsEnabled && topicsUiState.topics.isEmpty()) {
                        item(key = "topics_empty") {
                            TextButton(
                                onClick = { topicsViewModel.showAddTopicDialog() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                            ) {
                                Text(
                                    text = stringResource(R.string.add_first_topic),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── General task dialogs ───────────────────────────────────────────
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

        // ── Topic dialogs ──────────────────────────────────────────────────
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

        if (topicsUiState.showAddTaskDialog && topicsUiState.selectedTopic != null) {
            TaskDialog(
                title = stringResource(R.string.new_task_in_topic, topicsUiState.selectedTopic!!.name),
                initialValue = "",
                confirmLabel = stringResource(R.string.add),
                onDismiss = { topicsViewModel.hideAddTaskDialog() },
                onConfirm = { title ->
                    topicsViewModel.addTaskToTopic(title, topicsUiState.selectedTopic!!.id)
                    topicsViewModel.hideAddTaskDialog()
                }
            )
        }

        if (topicsUiState.showDeleteTopicDialog && topicsUiState.selectedTopic != null) {
            AlertDialog(
                onDismissRequest = { topicsViewModel.hideDeleteTopicDialog() },
                title = { Text(stringResource(R.string.delete_topic)) },
                text = { Text(stringResource(R.string.delete_topic_message, topicsUiState.selectedTopic!!.name)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            topicsViewModel.deleteTopic(topicsUiState.selectedTopic!!)
                            topicsViewModel.hideDeleteTopicDialog()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
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

// ── Topic card with its own task list ─────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopicCard(
    topic: Topic,
    topicsViewModel: TopicsViewModel,
    modifier: Modifier = Modifier
) {
    // Each topic card observes only its own tasks — no unnecessary recompositions
    val tasks by topicsViewModel.getTasksForTopic(topic.id).collectAsStateWithLifecycle()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Topic header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { topicsViewModel.showDeleteTopicDialog(topic) }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (tasks.isNotEmpty()) {
                        Text(
                            text = "${tasks.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    IconButton(
                        onClick = { topicsViewModel.showAddTaskDialog(topic) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add),
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Topic tasks — compact size
            if (tasks.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_tasks_in_topic),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            } else {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
                tasks.forEach { task ->
                    TopicTaskRow(
                        task = task,
                        onComplete = { topicsViewModel.completeTask(task) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun TopicTaskRow(
    task: Task,
    onComplete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = { onComplete() },
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )
    }
}

// ── Swipe to complete wrapper ─────────────────────────────────────────────────
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

// ── Regular task card ─────────────────────────────────────────────────────────
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

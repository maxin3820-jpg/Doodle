package com.doodle.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doodle.app.R
import com.doodle.app.data.model.Task
import com.doodle.app.ui.components.*
import com.doodle.app.ui.viewmodel.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                onClick = { viewModel.showAddDialog() },
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
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.activeTasks,
                        key = { it.id }
                    ) { task ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(250)) + expandVertically(),
                            exit = fadeOut(animationSpec = tween(250)) + shrinkVertically(),
                            modifier = Modifier.animateItemPlacement()
                        ) {
                            TaskCard(
                                task = task,
                                onCheckedChange = { viewModel.completeTask(task) },
                                onLongClick = { viewModel.showEditDialog(task) }
                            )
                        }
                    }
                }
            }
        }

        if (uiState.showAddDialog) {
            TaskDialog(
                title = stringResource(R.string.new_task),
                initialValue = "",
                onDismiss = { viewModel.hideAddDialog() },
                onConfirm = { title ->
                    viewModel.addTask(title)
                    viewModel.hideAddDialog()
                }
            )
        }

        if (uiState.showEditDialog && uiState.editingTask != null) {
            TaskEditDialog(
                task = uiState.editingTask!!,
                onDismiss = { viewModel.hideEditDialog() },
                onEdit = { title ->
                    viewModel.updateTask(uiState.editingTask!!.copy(title = title))
                    viewModel.hideEditDialog()
                },
                onDelete = {
                    viewModel.hideEditDialog()
                    viewModel.showDeleteDialog(uiState.editingTask!!)
                }
            )
        }

        if (uiState.showDeleteDialog && uiState.taskToDelete != null) {
            DeleteConfirmationDialog(
                onDismiss = { viewModel.hideDeleteDialog() },
                onConfirm = {
                    viewModel.deleteTask(uiState.taskToDelete!!)
                    viewModel.hideDeleteDialog()
                }
            )
        }
    }
}

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
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
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
                onCheckedChange = { onCheckedChange() }
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

package com.doodle.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doodle.app.R
import com.doodle.app.data.model.Task
import com.doodle.app.ui.components.EmptyState
import com.doodle.app.ui.viewmodel.CompletedViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CompletedScreen(
    viewModel: CompletedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.completed)) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.completedTasks.isEmpty()) {
                EmptyState(
                    title = stringResource(R.string.no_completed_tasks),
                    description = "",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.completedTasks,
                        key = { it.id }
                    ) { task ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(250)) + expandVertically(),
                            exit = fadeOut(animationSpec = tween(250)) + shrinkVertically(),
                            modifier = Modifier.animateItemPlacement()
                        ) {
                            CompletedTaskCard(
                                task = task,
                                onCheckedChange = { viewModel.uncompleteTask(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompletedTaskCard(
    task: Task,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                checked = true,
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

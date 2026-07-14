package com.doodle.app.ui.preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.doodle.app.data.model.Task
import com.doodle.app.data.settings.*
import com.doodle.app.ui.components.EmptyState
import com.doodle.app.ui.screens.CompletedTaskCard
import com.doodle.app.ui.screens.SettingsOptionItem
import com.doodle.app.ui.screens.SettingsSection
import com.doodle.app.ui.screens.TaskCard
import com.doodle.app.ui.theme.DoodleTheme

// ─── Sample Data ──────────────────────────────────────────────────────────────

private val sampleTasks = listOf(
    Task(1, "Buy groceries from the supermarket", false),
    Task(2, "Call the dentist and schedule appointment", false),
    Task(3, "Finish reading the book", false),
    Task(4, "Pay electricity bill online", false),
    Task(5, "Walk the dog in the evening", false),
)

private val sampleCompletedTasks = listOf(
    Task(6, "Send project report to manager", true),
    Task(7, "Clean the kitchen", true),
    Task(8, "Reply to all pending emails", true),
)

// ─── Tasks Screen – Light ─────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(name = "Tasks Screen – Light", showBackground = true,
    widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable
fun TasksScreenLightPreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Blue) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tasks") },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Tasks") },
                        selected = true,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                        label = { Text("Completed") },
                        selected = false,
                        onClick = {}
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleTasks.size) { index ->
                    TaskCard(
                        task = sampleTasks[index],
                        onCheckedChange = {},
                        onLongClick = {}
                    )
                }
            }
        }
    }
}

// ─── Tasks Screen – Dark ─────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(name = "Tasks Screen – Dark", showBackground = true,
    widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable
fun TasksScreenDarkPreview() {
    DoodleTheme(themeMode = ThemeMode.Dark, accentColor = AccentColor.Blue) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tasks") },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Tasks") },
                        selected = true,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                        label = { Text("Completed") },
                        selected = false,
                        onClick = {}
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleTasks.size) { index ->
                    TaskCard(
                        task = sampleTasks[index],
                        onCheckedChange = {},
                        onLongClick = {}
                    )
                }
            }
        }
    }
}

// ─── Tasks Screen – Empty State ───────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Tasks Screen – Empty", showBackground = true,
    widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable
fun TasksScreenEmptyPreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Purple) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tasks") },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Tasks") },
                        selected = true,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                        label = { Text("Completed") },
                        selected = false,
                        onClick = {}
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                EmptyState(
                    title = "No tasks yet.",
                    description = "If something needs to be done, add it.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

// ─── Completed Screen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Completed Screen – Light", showBackground = true,
    widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable
fun CompletedScreenPreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Green) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Completed") })
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Tasks") },
                        selected = false,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                        label = { Text("Completed") },
                        selected = true,
                        onClick = {}
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleCompletedTasks.size) { index ->
                    CompletedTaskCard(
                        task = sampleCompletedTasks[index],
                        onCheckedChange = {}
                    )
                }
            }
        }
    }
}

// ─── Settings Screen ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Settings Screen – Light", showBackground = true,
    widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Blue) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SettingsSection(title = "Appearance") {
                    // Theme
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Theme", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SettingsOptionItem("Light", isSelected = true, onClick = {})
                            SettingsOptionItem("Dark", isSelected = false, onClick = {})
                            SettingsOptionItem("Follow System", isSelected = false, onClick = {})
                        }
                    }
                    // Accent Color
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Accent Color", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SettingsOptionItem("Blue", isSelected = true, onClick = {})
                            SettingsOptionItem("Green", isSelected = false, onClick = {})
                            SettingsOptionItem("Purple", isSelected = false, onClick = {})
                        }
                    }
                }
                SettingsSection(title = "Typography") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Font Size", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SettingsOptionItem("Small", isSelected = false, onClick = {})
                            SettingsOptionItem("Medium", isSelected = true, onClick = {})
                            SettingsOptionItem("Large", isSelected = false, onClick = {})
                        }
                    }
                }
            }
        }
    }
}

// ─── Add Task Dialog ──────────────────────────────────────────────────────────

@Preview(name = "Add Task Dialog", showBackground = true,
    widthDp = 390, heightDp = 844)
@Composable
fun AddTaskDialogPreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Blue) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("New Task") },
                text = {
                    OutlinedTextField(
                        value = "Buy milk and eggs",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 5
                    )
                },
                confirmButton = {
                    TextButton(onClick = {}) { Text("Add") }
                },
                dismissButton = {
                    TextButton(onClick = {}) { Text("Cancel") }
                }
            )
        }
    }
}

// ─── Accent Color Variants ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(name = "Accent – Green", showBackground = true, widthDp = 390, heightDp = 400)
@Composable
fun AccentGreenPreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Green) {
        Scaffold(topBar = { TopAppBar(title = { Text("Tasks") }) }) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) { i ->
                    TaskCard(task = sampleTasks[i], onCheckedChange = {}, onLongClick = {})
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(name = "Accent – Purple", showBackground = true, widthDp = 390, heightDp = 400)
@Composable
fun AccentPurplePreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Purple) {
        Scaffold(topBar = { TopAppBar(title = { Text("Tasks") }) }) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) { i ->
                    TaskCard(task = sampleTasks[i], onCheckedChange = {}, onLongClick = {})
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(name = "Accent – Orange", showBackground = true, widthDp = 390, heightDp = 400)
@Composable
fun AccentOrangePreview() {
    DoodleTheme(themeMode = ThemeMode.Light, accentColor = AccentColor.Orange) {
        Scaffold(topBar = { TopAppBar(title = { Text("Tasks") }) }) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) { i ->
                    TaskCard(task = sampleTasks[i], onCheckedChange = {}, onLongClick = {})
                }
            }
        }
    }
}

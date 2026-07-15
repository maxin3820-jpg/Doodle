package com.doodle.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.doodle.app.R
import com.doodle.app.data.settings.*
import com.doodle.app.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val accentColor by viewModel.accentColor.collectAsStateWithLifecycle()
    val backgroundColor by viewModel.backgroundColor.collectAsStateWithLifecycle()
    val fontFamily by viewModel.fontFamily.collectAsStateWithLifecycle()
    val fontSize by viewModel.fontSize.collectAsStateWithLifecycle()
    val topicsEnabled by viewModel.topicsEnabled.collectAsStateWithLifecycle()
    val activeTaskCount by viewModel.activeTaskCount.collectAsStateWithLifecycle()
    val completedTaskCount by viewModel.completedTaskCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
            // Options lists are remembered — no new list allocated on every recomposition
            val themeOptions = remember {
                listOf("Light" to ThemeMode.Light, "Dark" to ThemeMode.Dark, "Follow System" to ThemeMode.FollowSystem)
            }
            val accentOptions = remember {
                listOf("Blue" to AccentColor.Blue, "Green" to AccentColor.Green, "Purple" to AccentColor.Purple,
                    "Orange" to AccentColor.Orange, "Red" to AccentColor.Red, "Pink" to AccentColor.Pink)
            }
            val backgroundOptions = remember {
                listOf("White" to BackgroundColor.White, "Black" to BackgroundColor.Black,
                    "Light Gray" to BackgroundColor.LightGray, "Cream" to BackgroundColor.Cream,
                    "Soft Blue" to BackgroundColor.SoftBlue, "Soft Green" to BackgroundColor.SoftGreen)
            }
            val fontFamilyOptions = remember {
                listOf("Roboto" to FontFamily.Roboto, "Inter" to FontFamily.Inter,
                    "Poppins" to FontFamily.Poppins, "Nunito" to FontFamily.Nunito, "Open Sans" to FontFamily.OpenSans)
            }
            val fontSizeOptions = remember {
                listOf("Small" to FontSize.Small, "Medium" to FontSize.Medium,
                    "Large" to FontSize.Large, "Extra Large" to FontSize.ExtraLarge)
            }

            SettingsSection(title = stringResource(R.string.appearance)) {
                SettingsOptionSelector(
                    title = stringResource(R.string.theme),
                    options = themeOptions,
                    selectedOption = themeMode,
                    onOptionSelected = { viewModel.setThemeMode(it) }
                )
                SettingsOptionSelector(
                    title = stringResource(R.string.accent_color),
                    options = accentOptions,
                    selectedOption = accentColor,
                    onOptionSelected = { viewModel.setAccentColor(it) }
                )
                SettingsOptionSelector(
                    title = stringResource(R.string.background_color),
                    options = backgroundOptions,
                    selectedOption = backgroundColor,
                    onOptionSelected = { viewModel.setBackgroundColor(it) }
                )
            }

            SettingsSection(title = stringResource(R.string.typography)) {
                SettingsOptionSelector(
                    title = stringResource(R.string.font_family),
                    options = fontFamilyOptions,
                    selectedOption = fontFamily,
                    onOptionSelected = { viewModel.setFontFamily(it) }
                )
                SettingsOptionSelector(
                    title = stringResource(R.string.font_size),
                    options = fontSizeOptions,
                    selectedOption = fontSize,
                    onOptionSelected = { viewModel.setFontSize(it) }
                )
            }

            SettingsSection(title = stringResource(R.string.features)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.topics),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(R.string.topics_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = topicsEnabled,
                        onCheckedChange = { viewModel.setTopicsEnabled(it) }
                    )
                }
            }

            // Task statistics section
            SettingsSection(title = stringResource(R.string.statistics)) {
                // Active tasks count
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.active_tasks),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "$activeTaskCount",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Divider()
                
                // Completed tasks count
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.completed_tasks_count),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "$completedTaskCount",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
fun <T> SettingsOptionSelector(
    title: String,
    options: List<Pair<String, T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { (label, value) ->
                SettingsOptionItem(
                    label = label,
                    isSelected = selectedOption == value,
                    onClick = { onOptionSelected(value) }
                )
            }
        }
    }
}

@Composable
fun SettingsOptionItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}

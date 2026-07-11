package com.doodle.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            SettingsSection(title = stringResource(R.string.appearance)) {
                SettingsOptionSelector(
                    title = stringResource(R.string.theme),
                    options = listOf(
                        stringResource(R.string.light) to ThemeMode.Light,
                        stringResource(R.string.dark) to ThemeMode.Dark,
                        stringResource(R.string.follow_system) to ThemeMode.FollowSystem
                    ),
                    selectedOption = themeMode,
                    onOptionSelected = { viewModel.setThemeMode(it) }
                )

                SettingsOptionSelector(
                    title = stringResource(R.string.accent_color),
                    options = listOf(
                        "Blue" to AccentColor.Blue,
                        "Green" to AccentColor.Green,
                        "Purple" to AccentColor.Purple,
                        "Orange" to AccentColor.Orange,
                        "Red" to AccentColor.Red,
                        "Pink" to AccentColor.Pink
                    ),
                    selectedOption = accentColor,
                    onOptionSelected = { viewModel.setAccentColor(it) }
                )

                SettingsOptionSelector(
                    title = stringResource(R.string.background_color),
                    options = listOf(
                        "White" to BackgroundColor.White,
                        "Black" to BackgroundColor.Black,
                        "Light Gray" to BackgroundColor.LightGray,
                        "Cream" to BackgroundColor.Cream,
                        "Soft Blue" to BackgroundColor.SoftBlue,
                        "Soft Green" to BackgroundColor.SoftGreen
                    ),
                    selectedOption = backgroundColor,
                    onOptionSelected = { viewModel.setBackgroundColor(it) }
                )
            }

            SettingsSection(title = stringResource(R.string.typography)) {
                SettingsOptionSelector(
                    title = stringResource(R.string.font_family),
                    options = listOf(
                        "Roboto" to FontFamily.Roboto,
                        "Inter" to FontFamily.Inter,
                        "Poppins" to FontFamily.Poppins,
                        "Nunito" to FontFamily.Nunito,
                        "Open Sans" to FontFamily.OpenSans
                    ),
                    selectedOption = fontFamily,
                    onOptionSelected = { viewModel.setFontFamily(it) }
                )

                SettingsOptionSelector(
                    title = stringResource(R.string.font_size),
                    options = listOf(
                        "Small" to FontSize.Small,
                        "Medium" to FontSize.Medium,
                        "Large" to FontSize.Large,
                        "Extra Large" to FontSize.ExtraLarge
                    ),
                    selectedOption = fontSize,
                    onOptionSelected = { viewModel.setFontSize(it) }
                )
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

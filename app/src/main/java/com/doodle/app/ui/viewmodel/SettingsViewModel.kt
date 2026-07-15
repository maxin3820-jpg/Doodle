package com.doodle.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.app.data.settings.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettings: AppSettings,
    private val taskRepository: com.doodle.app.data.repository.TaskRepository
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = appSettings.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.FollowSystem)

    val accentColor: StateFlow<AccentColor> = appSettings.accentColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, AccentColor.Blue)

    val backgroundColor: StateFlow<BackgroundColor> = appSettings.backgroundColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, BackgroundColor.White)

    val fontFamily: StateFlow<FontFamily> = appSettings.fontFamily
        .stateIn(viewModelScope, SharingStarted.Eagerly, FontFamily.Roboto)

    val fontSize: StateFlow<FontSize> = appSettings.fontSize
        .stateIn(viewModelScope, SharingStarted.Eagerly, FontSize.Medium)

    val topicsEnabled: StateFlow<Boolean> = appSettings.topicsEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Task counts for statistics in settings
    val activeTaskCount: StateFlow<Int> = taskRepository.getActiveTasks()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val completedTaskCount: StateFlow<Int> = taskRepository.getCompletedTasks()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            appSettings.setThemeMode(themeMode)
        }
    }

    fun setAccentColor(accentColor: AccentColor) {
        viewModelScope.launch {
            appSettings.setAccentColor(accentColor)
        }
    }

    fun setBackgroundColor(backgroundColor: BackgroundColor) {
        viewModelScope.launch {
            appSettings.setBackgroundColor(backgroundColor)
        }
    }

    fun setFontFamily(fontFamily: FontFamily) {
        viewModelScope.launch {
            appSettings.setFontFamily(fontFamily)
        }
    }

    fun setFontSize(fontSize: FontSize) {
        viewModelScope.launch {
            appSettings.setFontSize(fontSize)
        }
    }

    fun setTopicsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            appSettings.setTopicsEnabled(enabled)
        }
    }
}

package com.doodle.app.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class AppSettings @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val ACCENT_COLOR = stringPreferencesKey("accent_color")
        private val BACKGROUND_COLOR = stringPreferencesKey("background_color")
        private val FONT_FAMILY = stringPreferencesKey("font_family")
        private val FONT_SIZE = stringPreferencesKey("font_size")
    }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { preferences ->
        when (preferences[THEME_MODE]) {
            "light" -> ThemeMode.Light
            "dark" -> ThemeMode.Dark
            else -> ThemeMode.FollowSystem
        }
    }

    val accentColor: Flow<AccentColor> = dataStore.data.map { preferences ->
        when (preferences[ACCENT_COLOR]) {
            "blue" -> AccentColor.Blue
            "green" -> AccentColor.Green
            "purple" -> AccentColor.Purple
            "orange" -> AccentColor.Orange
            "red" -> AccentColor.Red
            "pink" -> AccentColor.Pink
            else -> AccentColor.Blue
        }
    }

    val backgroundColor: Flow<BackgroundColor> = dataStore.data.map { preferences ->
        when (preferences[BACKGROUND_COLOR]) {
            "white" -> BackgroundColor.White
            "black" -> BackgroundColor.Black
            "light_gray" -> BackgroundColor.LightGray
            "cream" -> BackgroundColor.Cream
            "soft_blue" -> BackgroundColor.SoftBlue
            "soft_green" -> BackgroundColor.SoftGreen
            else -> BackgroundColor.White
        }
    }

    val fontFamily: Flow<FontFamily> = dataStore.data.map { preferences ->
        when (preferences[FONT_FAMILY]) {
            "roboto" -> FontFamily.Roboto
            "inter" -> FontFamily.Inter
            "poppins" -> FontFamily.Poppins
            "nunito" -> FontFamily.Nunito
            "open_sans" -> FontFamily.OpenSans
            else -> FontFamily.Roboto
        }
    }

    val fontSize: Flow<FontSize> = dataStore.data.map { preferences ->
        when (preferences[FONT_SIZE]) {
            "small" -> FontSize.Small
            "medium" -> FontSize.Medium
            "large" -> FontSize.Large
            "extra_large" -> FontSize.ExtraLarge
            else -> FontSize.Medium
        }
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = when (themeMode) {
                ThemeMode.Light -> "light"
                ThemeMode.Dark -> "dark"
                ThemeMode.FollowSystem -> "follow_system"
            }
        }
    }

    suspend fun setAccentColor(accentColor: AccentColor) {
        dataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = when (accentColor) {
                AccentColor.Blue -> "blue"
                AccentColor.Green -> "green"
                AccentColor.Purple -> "purple"
                AccentColor.Orange -> "orange"
                AccentColor.Red -> "red"
                AccentColor.Pink -> "pink"
            }
        }
    }

    suspend fun setBackgroundColor(backgroundColor: BackgroundColor) {
        dataStore.edit { preferences ->
            preferences[BACKGROUND_COLOR] = when (backgroundColor) {
                BackgroundColor.White -> "white"
                BackgroundColor.Black -> "black"
                BackgroundColor.LightGray -> "light_gray"
                BackgroundColor.Cream -> "cream"
                BackgroundColor.SoftBlue -> "soft_blue"
                BackgroundColor.SoftGreen -> "soft_green"
            }
        }
    }

    suspend fun setFontFamily(fontFamily: FontFamily) {
        dataStore.edit { preferences ->
            preferences[FONT_FAMILY] = when (fontFamily) {
                FontFamily.Roboto -> "roboto"
                FontFamily.Inter -> "inter"
                FontFamily.Poppins -> "poppins"
                FontFamily.Nunito -> "nunito"
                FontFamily.OpenSans -> "open_sans"
            }
        }
    }

    suspend fun setFontSize(fontSize: FontSize) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE] = when (fontSize) {
                FontSize.Small -> "small"
                FontSize.Medium -> "medium"
                FontSize.Large -> "large"
                FontSize.ExtraLarge -> "extra_large"
            }
        }
    }
}

enum class ThemeMode {
    Light, Dark, FollowSystem
}

enum class AccentColor {
    Blue, Green, Purple, Orange, Red, Pink
}

enum class BackgroundColor {
    White, Black, LightGray, Cream, SoftBlue, SoftGreen
}

enum class FontFamily {
    Roboto, Inter, Poppins, Nunito, OpenSans
}

enum class FontSize {
    Small, Medium, Large, ExtraLarge
}

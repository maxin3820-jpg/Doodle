package com.doodle.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.doodle.app.ui.navigation.NavGraph
import com.doodle.app.ui.navigation.Screen
import com.doodle.app.ui.screens.CompletedScreen
import com.doodle.app.ui.screens.TasksScreen
import com.doodle.app.ui.theme.DoodleTheme
import com.doodle.app.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoodleApp()
        }
    }
}

@Composable
fun DoodleApp(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val themeMode by settingsViewModel.themeMode.collectAsStateWithLifecycle()
    val accentColor by settingsViewModel.accentColor.collectAsStateWithLifecycle()
    val backgroundColor by settingsViewModel.backgroundColor.collectAsStateWithLifecycle()
    val fontFamily by settingsViewModel.fontFamily.collectAsStateWithLifecycle()
    val fontSize by settingsViewModel.fontSize.collectAsStateWithLifecycle()

    DoodleTheme(
        themeMode = themeMode,
        accentColor = accentColor,
        backgroundColor = backgroundColor,
        fontFamily = fontFamily,
        fontSize = fontSize
    ) {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Settings.route) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Tasks") },
                        selected = currentRoute == Screen.Tasks.route,
                        onClick = {
                            if (currentRoute != Screen.Tasks.route) {
                                navController.navigate(Screen.Tasks.route) {
                                    popUpTo(Screen.Tasks.route) { inclusive = true }
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                        label = { Text("Completed") },
                        selected = currentRoute == Screen.Completed.route,
                        onClick = {
                            if (currentRoute != Screen.Completed.route) {
                                navController.navigate(Screen.Completed.route) {
                                    popUpTo(Screen.Tasks.route)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(
                navController = navController,
                startDestination = Screen.Tasks.route,
                tasksContent = {
                    TasksScreen(
                        onNavigateToSettings = {
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                },
                completedContent = {
                    CompletedScreen()
                }
            )
        }
    }
}

@Composable
fun DoodleApp(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val themeMode by settingsViewModel.themeMode.collectAsStateWithLifecycle()
    val accentColor by settingsViewModel.accentColor.collectAsStateWithLifecycle()
    val backgroundColor by settingsViewModel.backgroundColor.collectAsStateWithLifecycle()
    val fontFamily by settingsViewModel.fontFamily.collectAsStateWithLifecycle()
    val fontSize by settingsViewModel.fontSize.collectAsStateWithLifecycle()

    DoodleTheme(
        themeMode = themeMode,
        accentColor = accentColor,
        backgroundColor = backgroundColor,
        fontFamily = fontFamily,
        fontSize = fontSize
    ) {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Settings.route) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Tasks") },
                        selected = currentRoute == Screen.Tasks.route,
                        onClick = {
                            if (currentRoute != Screen.Tasks.route) {
                                navController.navigate(Screen.Tasks.route) {
                                    popUpTo(Screen.Tasks.route) { inclusive = true }
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                        label = { Text("Completed") },
                        selected = currentRoute == Screen.Completed.route,
                        onClick = {
                            if (currentRoute != Screen.Completed.route) {
                                navController.navigate(Screen.Completed.route) {
                                    popUpTo(Screen.Tasks.route)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(
                navController = navController,
                startDestination = Screen.Tasks.route,
                tasksContent = {
                    TasksScreen(
                        onNavigateToSettings = {
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                },
                completedContent = {
                    CompletedScreen()
                }
            )
        }
    }
}

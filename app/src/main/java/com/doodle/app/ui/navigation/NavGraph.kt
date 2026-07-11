package com.doodle.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.doodle.app.ui.screens.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Tasks.route,
    tasksContent: @Composable () -> Unit,
    completedContent: @Composable () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Tasks.route) {
            tasksContent()
        }
        composable(Screen.Completed.route) {
            completedContent()
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Tasks : Screen("tasks")
    object Completed : Screen("completed")
    object Settings : Screen("settings")
}

package com.doodle.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.doodle.app.ui.screens.SettingsScreen
import com.doodle.app.ui.screens.TopicDetailScreen

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
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.TopicDetail.route,
            arguments = listOf(
                navArgument("topicId") { type = NavType.LongType },
                navArgument("topicName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong("topicId") ?: return@composable
            val topicName = backStackEntry.arguments?.getString("topicName") ?: ""
            TopicDetailScreen(
                topicId = topicId,
                topicName = topicName,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Tasks : Screen("tasks")
    object Completed : Screen("completed")
    object Settings : Screen("settings")
    object TopicDetail : Screen("topic/{topicId}/{topicName}") {
        fun createRoute(topicId: Long, topicName: String) =
            "topic/$topicId/${topicName.replace("/", "-")}"
    }
}

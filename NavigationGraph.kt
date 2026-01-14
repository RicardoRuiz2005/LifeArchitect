package com.example.lifearchitect

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    // IMPORTANTE: Esta forma de inicializar busca automáticamente
    // el Application Context necesario para Room.
    val taskViewModel: TaskViewModel = viewModel()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(taskViewModel) { navController.navigate("tasks") }
        }
        composable("tasks") {
            TaskListScreen(
                viewModel = taskViewModel,
                onAddTaskClick = { navController.navigate("add_task") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("add_task") {
            AddTaskScreen(taskViewModel) { navController.popBackStack() }
        }
    }
}

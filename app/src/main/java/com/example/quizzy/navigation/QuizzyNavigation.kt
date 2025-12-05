// File: navigation/QuizzyNavigation.kt
// Location: app/src/main/java/com/example/quizzy/navigation/QuizzyNavigation.kt
package com.example.quizzy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizzy.data.repository.AuthRepository
import com.example.quizzy.presentation.home.HomeScreen
import com.example.quizzy.presentation.login.LoginScreen
import com.example.quizzy.presentation.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Settings : Screen("settings")
}

@Composable
fun QuizzyNavigation(
    navController: NavHostController = rememberNavController(),
    authRepository: AuthRepository
) {
    val isAuthenticated by authRepository.getAuthStateFlow().collectAsState(initial = false)

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated && navController.currentDestination?.route != Screen.Login.route) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authRepository.isUserAuthenticated()) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
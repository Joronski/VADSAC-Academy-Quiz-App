package com.example.finalscasestudy.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.finalscasestudy.ui.home.HomeScreen
import com.example.finalscasestudy.ui.login.LoginScreen
import com.example.finalscasestudy.ui.login.RegisterScreen
import com.example.finalscasestudy.ui.quiz.*
import com.example.finalscasestudy.ui.viewmodel.UserViewModel
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    quizViewModel: QuizViewModel
) {
    NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {

        // Login Screen
        composable(NavRoutes.LOGIN) {
            LoginScreen(navController = navController, userViewModel = userViewModel)
        }

        // Register Screen
        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController = navController, userViewModel = userViewModel)
        }

        // Home Screen
        composable(NavRoutes.HOME) {
            HomeScreen(navController = navController, userViewModel = userViewModel)
        }

        // Quiz Category Selection Screen
        composable(NavRoutes.SELECT_QUIZ_CATEGORY) {
            QuizCategorySelectionScreen(
                navController = navController,
                quizViewModel = quizViewModel
            )
        }

        // Quiz Difficulty Selection Screen
        composable(
            route = NavRoutes.SELECT_QUIZ_DIFFICULTY,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            QuizDifficultySelectionScreen(
                category = category,
                navController = navController,
                quizViewModel = quizViewModel,
                userViewModel = userViewModel
            )
        }

        // Quiz Answering Screen
        composable(NavRoutes.QUIZ_ANSWERING) {
            QuizAnsweringScreen(
                navController = navController,
                quizViewModel = quizViewModel,
                userViewModel = userViewModel
            )
        }

        // Quiz Result Screen
        composable(NavRoutes.QUIZ_RESULT) {
            QuizResultScreen(
                navController = navController,
                quizViewModel = quizViewModel,
                userViewModel = userViewModel
            )
        }

        // Quiz Records Screen
        composable(NavRoutes.QUIZ_RECORDS) {
            QuizRecordsScreen(
                navController = navController,
                quizViewModel = quizViewModel,
                userViewModel = userViewModel
            )
        }
    }
}

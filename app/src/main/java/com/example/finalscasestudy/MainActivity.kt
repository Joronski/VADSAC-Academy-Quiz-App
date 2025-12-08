package com.example.finalscasestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.finalscasestudy.data.local.DatabaseBuilder
import com.example.finalscasestudy.data.repository.QuizAttemptRepository
import com.example.finalscasestudy.data.repository.QuizRepository
import com.example.finalscasestudy.data.repository.UserRepository
import com.example.finalscasestudy.ui.nav.AppNavGraph
import com.example.finalscasestudy.ui.theme.FinalsCaseStudyTheme
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel
import com.example.finalscasestudy.ui.viewmodel.QuizViewModelFactory
import com.example.finalscasestudy.ui.viewmodel.UserViewModel
import com.example.finalscasestudy.ui.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = DatabaseBuilder.getDatabase(applicationContext)

        val userRepository = UserRepository(db.userDao())
        val quizRepository = QuizRepository(db.quizDao())
        val quizAttemptRepository = QuizAttemptRepository(db.quizAttemptDao())

        setContent {
            FinalsCaseStudyTheme {
                val navController = rememberNavController()

                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(userRepository)
                )
                val quizViewModel: QuizViewModel = viewModel(
                    factory = QuizViewModelFactory(quizRepository, quizAttemptRepository)
                )

                AppNavGraph(
                    navController = navController,
                    userViewModel = userViewModel,
                    quizViewModel = quizViewModel
                )
            }
        }
    }
}

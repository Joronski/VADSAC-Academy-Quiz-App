package com.example.finalscasestudy.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalscasestudy.ui.commons.ErrorDialog
import com.example.finalscasestudy.ui.commons.QuizCard
import com.example.finalscasestudy.ui.nav.NavRoutes
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel
import com.example.finalscasestudy.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDifficultySelectionScreen(
    category: String,
    navController: NavController,
    quizViewModel: QuizViewModel,
    userViewModel: UserViewModel
) {
    val error by quizViewModel.error.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()

    // Difficulty options
    val quizzes = listOf(
        Pair("$category Quiz 1", "Easy"),
        Pair("$category Quiz 2", "Medium"),
        Pair("$category Quiz 3", "Hard")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = { Text("QuizIT", fontSize = 25.sp, fontWeight = FontWeight.SemiBold) }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    thickness = 5.dp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 25.dp, bottom = 20.dp),
                text = "Select Quiz",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(quizzes) { quiz ->
                    QuizCard(
                        quizName = quiz.first,
                        difficulty = quiz.second,
                        onClick = {
                            // Only load quiz if the user is logged in
                            currentUser?.email?.let { email ->
                                quizViewModel.loadQuiz(category, quiz.second, email)
                                navController.navigate(NavRoutes.QUIZ_ANSWERING)
                            }
                        }
                    )
                }
            }
        }

        // Error Dialog
        error?.let { errorMessage ->
            ErrorDialog(
                message = errorMessage,
                onDismiss = { quizViewModel.resetError() }
            )
        }
    }
}

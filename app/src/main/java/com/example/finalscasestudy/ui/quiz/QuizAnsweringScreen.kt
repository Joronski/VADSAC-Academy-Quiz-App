package com.example.finalscasestudy.ui.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalscasestudy.ui.commons.ErrorDialog
import com.example.finalscasestudy.ui.nav.NavRoutes
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel
import com.example.finalscasestudy.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizAnsweringScreen(
    navController: NavController,
    quizViewModel: QuizViewModel,
    userViewModel: UserViewModel
) {
    val questions by quizViewModel.questions.collectAsState()
    val currentIndex by quizViewModel.currentIndex.collectAsState()
    val timer by quizViewModel.timer.collectAsState()
    val completed by quizViewModel.completed.collectAsState()
    val error by quizViewModel.error.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()
    val userEmail = currentUser?.email ?: ""

    // Navigate to result screen when quiz is completed
    if (completed) {
        navController.navigate(NavRoutes.QUIZ_RESULT) {
            popUpTo(NavRoutes.QUIZ_ANSWERING) { inclusive = true }
        }
    }

    // Handle system back press to reset quiz
    BackHandler {
        quizViewModel.resetQuiz()
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = {
                        Text(
                            text = "QuizIT",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 5.dp)
            }
        }
    ) { innerPadding ->

        // Error dialog
        error?.let { errorMessage ->
            ErrorDialog(
                message = errorMessage,
                onDismiss = {
                    quizViewModel.resetError()
                    navController.popBackStack()
                }
            )
        }

        if (questions.isEmpty() && error == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (questions.isNotEmpty()) {
            val question = questions[currentIndex]
            val selectedAnswerIndex = quizViewModel.getSelectedAnswerIndexForCurrentQuestion()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer and question index
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 10.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.height(70.dp),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        Column(
                            modifier = Modifier.size(60.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.Center),
                                text = "${currentIndex + 1}/${questions.size}",
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "${timer}s",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                // Question text
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Question ${currentIndex + 1}:",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = question.questionText,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Justify
                    )
                }

                // Answer options
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    itemsIndexed(question.answers) { index, answer ->
                        if (answer != null) {
                            val isSelected = selectedAnswerIndex == index
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { quizViewModel.selectAnswer(index) },
                                shape = RoundedCornerShape(15.dp),
                                elevation = CardDefaults.cardElevation(5.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF90CAF9) else Color(0xFFE0E0E0)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(15.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = "${'A' + index}. $answer",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }

                    // Submit button
                    items(1) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                onClick = { quizViewModel.submitCurrentAnswer() } // Pass email when submitting
                            ) {
                                Text(
                                    text = "Submit",
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

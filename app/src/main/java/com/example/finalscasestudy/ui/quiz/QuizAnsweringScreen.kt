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
import com.example.finalscasestudy.ui.theme.Blue40
import com.example.finalscasestudy.ui.theme.LightGray
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizAnsweringScreen(
    navController: NavController,
    quizViewModel: QuizViewModel
) {
    val questions by quizViewModel.questions.collectAsState()
    val currentIndex by quizViewModel.currentIndex.collectAsState()
    val timer by quizViewModel.timer.collectAsState()
    val completed by quizViewModel.completed.collectAsState()
    val error by quizViewModel.error.collectAsState()

    if (completed) {
        navController.navigate(NavRoutes.QUIZ_RESULT) {
            popUpTo(NavRoutes.QUIZ_ANSWERING) { inclusive = true }
        }
    }

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
                            text = "VADSAC Academy's QuizIT",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    thickness = 5.dp,
                    color = Blue40
                )
            }
        }
    ) { innerPadding ->

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
                CircularProgressIndicator(color = Blue40)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 16.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Blue40
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${currentIndex + 1}/${questions.size}",
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "${timer}s",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Question ${currentIndex + 1}:",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue40
                    )
                    Text(
                        text = question.questionText,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                                    containerColor = if (isSelected) Blue40 else LightGray
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = "${'A' + index}. $answer",
                                        fontSize = 18.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                onClick = { quizViewModel.submitCurrentAnswer() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue40
                                )
                            ) {
                                Text(
                                    text = "Submit",
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
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
package com.example.finalscasestudy.ui.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalscasestudy.ui.theme.Blue40
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel
import com.example.finalscasestudy.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultScreen(
    navController: NavController,
    quizViewModel: QuizViewModel,
    userViewModel: UserViewModel
) {
    val quiz = quizViewModel.quiz.collectAsState().value
    val questions = quizViewModel.questions.collectAsState().value
    val currentUser by userViewModel.currentUser.collectAsState()

    val totalQuestions = questions.size
    var correct = 0
    var wrong = 0
    var missed = 0

    questions.forEachIndexed { index, question ->
        when (quizViewModel.getSelectedAnswerIndexForCurrentQuestionAt(index)) {
            null -> missed++
            question.correctIndex -> correct++
            else -> wrong++
        }
    }

    val scoreText = "$correct/$totalQuestions"

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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "You scored",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    scoreText,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue40
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ResultCard(number = correct, label = "Correct")
                ResultCard(number = wrong, label = "Wrong")
                ResultCard(number = missed, label = "Missed")
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(questions.size) { index ->
                    val question = questions[index]
                    val selected = quizViewModel.getSelectedAnswerIndexForCurrentQuestionAt(index)

                    val borderColor = when {
                        selected == null -> Color.Gray
                        selected == question.correctIndex -> Color(0xFF4CAF50)
                        else -> Color(0xFFF44336)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(3.dp, borderColor)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Q${index + 1}: ${question.questionText}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Correct Answer: ${question.answers[question.correctIndex]}",
                                fontSize = 18.sp,
                                color = Blue40,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Retake Quiz Button
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 4.dp),
                        onClick = {
                            quiz?.let { q ->
                                currentUser?.email?.let { email ->
                                    quizViewModel.resetQuiz()
                                    quizViewModel.loadQuiz(q.category, q.difficulty, email)
                                    navController.navigate("quiz_answering") {
                                        popUpTo("quiz_result") { inclusive = true }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue40
                        )
                    ) {
                        Text("Retake Quiz", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                item {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 4.dp),
                        onClick = {
                            quizViewModel.resetQuiz()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Blue40
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Blue40)
                        )
                    ) {
                        Text("Go Back Home", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCard(number: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.size(80.dp),
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
                    text = number.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

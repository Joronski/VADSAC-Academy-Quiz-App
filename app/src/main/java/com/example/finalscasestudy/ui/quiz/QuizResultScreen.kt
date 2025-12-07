package com.example.finalscasestudy.ui.quiz

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    // Calculate results using userAnswers from this quiz only
    val totalQuestions = questions.size
    var correct = 0
    var wrong = 0
    var missed = 0

    questions.forEachIndexed { index, question ->
        when (val answer = quizViewModel.getSelectedAnswerIndexForCurrentQuestionAt(index)) {
            null -> missed++
            question.correctIndex -> correct++
            else -> wrong++
        }
    }

    val scoreText = "$correct/$totalQuestions"

    // Handle system back press to reset quiz state
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
                Divider(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    thickness = 5.dp
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Score display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("You scored", fontSize = 20.sp)
                Text(scoreText, fontSize = 50.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Correct / Wrong / Missed
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

            // Question review list
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(questions.size) { index ->
                    val question = questions[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "Q${index + 1}: ${question.questionText}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Correct Answer: ${question.answers[question.correctIndex]}",
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // Retake Quiz Button
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
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
                        }
                    ) {
                        Text("Retake Quiz", fontSize = 18.sp)
                    }
                }

                // Go Back Home Button
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        onClick = {
                            quizViewModel.resetQuiz()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Go Back Home", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCard(number: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    text = number.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}

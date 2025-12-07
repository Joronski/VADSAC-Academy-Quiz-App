package com.example.finalscasestudy.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel
import com.example.finalscasestudy.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizRecordsScreen(
    navController: NavController,
    quizViewModel: QuizViewModel,
    userViewModel: UserViewModel
) {
    val attempts by quizViewModel.quizAttempts.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()

    // Load attempts only for the current logged-in user
    LaunchedEffect(currentUser) {
        currentUser?.email?.let { email ->
            quizViewModel.loadQuizAttempts(email)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(title = { Text("QuizIT", fontSize = 25.sp) })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 5.dp)
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
                text = "Quiz Score History:",
                fontSize = 25.sp,
                modifier = Modifier.padding(15.dp)
            )

            if (attempts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No quiz records yet", fontSize = 18.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(attempts) { attempt ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .height(150.dp),
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(5.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.7f)
                                        .padding(start = 15.dp, top = 10.dp, bottom = 10.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Button(onClick = {
                                            // Retake quiz using current user email
                                            currentUser?.email?.let { email ->
                                                quizViewModel.loadQuiz(attempt.category, attempt.difficulty, email)
                                            }
                                            navController.navigate("quiz_answering")
                                        }) {
                                            Text("Retake")
                                        }

                                        Text(
                                            text = attempt.category,
                                            fontSize = 23.sp,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }

                                    Divider(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                        thickness = 2.dp
                                    )

                                    Row {
                                        Text("Difficulty:", fontSize = 15.sp)
                                        Text(attempt.difficulty, fontSize = 15.sp, modifier = Modifier.padding(start = 5.dp))
                                    }
                                }

                                Divider(
                                    modifier = Modifier.fillMaxHeight().width(2.dp)
                                )

                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("${attempt.score}", fontSize = 30.sp)
                                    Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp), thickness = 2.dp)
                                    Text("${attempt.totalQuestions}", fontSize = 30.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

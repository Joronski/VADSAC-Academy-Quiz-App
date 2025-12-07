package com.example.finalscasestudy.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalscasestudy.ui.theme.Blue40
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
                TopAppBar(
                    title = {
                        Text(
                            "VADSAC Academy's QuizIT",
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
                fontWeight = FontWeight.SemiBold,
                color = Blue40,
                modifier = Modifier.padding(15.dp)
            )

            if (attempts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No quiz records yet",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(attempts) { attempt ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .wrapContentHeight(),
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(5.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                // Retake quiz using current user email
                                                currentUser?.email?.let { email ->
                                                    quizViewModel.loadQuiz(attempt.category, attempt.difficulty, email)
                                                }
                                                navController.navigate("quiz_answering")
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Blue40
                                            )
                                        ) {
                                            Text("Retake", fontWeight = FontWeight.SemiBold)
                                        }

                                        Text(
                                            text = attempt.category,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    HorizontalDivider(
                                        thickness = 2.dp,
                                        color = Blue40
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        Text(
                                            "Difficulty:",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            attempt.difficulty,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                VerticalDivider(
                                    modifier = Modifier.height(100.dp),
                                    thickness = 2.dp,
                                    color = Blue40
                                )

                                Column(
                                    modifier = Modifier.padding(start = 16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "${attempt.score}",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Blue40
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .padding(vertical = 5.dp),
                                        thickness = 2.dp,
                                        color = Blue40
                                    )
                                    Text(
                                        "${attempt.totalQuestions}",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
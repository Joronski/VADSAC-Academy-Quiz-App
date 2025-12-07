package com.example.finalscasestudy.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalscasestudy.ui.commons.CategoryCard
import com.example.finalscasestudy.ui.nav.NavRoutes
import com.example.finalscasestudy.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizCategorySelectionScreen(
    navController: NavController,
    quizViewModel: QuizViewModel
) {
    // Hardcoded categories, could later come from ViewModel
    val categories = listOf("Linux", "BASH", "SQL", "Docker", "HTML")

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
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Quiz Category",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 10.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(categories) { category ->
                    CategoryCard(
                        categoryName = category,
                        onClick = {
                            // Load quizzes of this category (optional)
                            quizViewModel.resetQuiz()
                            navController.navigate("select_quiz_difficulty/$category")
                        }
                    )
                }
            }
        }
    }
}

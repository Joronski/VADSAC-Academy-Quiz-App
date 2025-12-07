package com.example.finalscasestudy.ui.home

import androidx.compose.foundation.layout.*
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
import com.example.finalscasestudy.ui.nav.NavRoutes
import com.example.finalscasestudy.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel) {

    val currentUser by userViewModel.currentUser.collectAsState()

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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Welcome to QuizIT",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                text = currentUser?.username ?: "Guest",
                fontSize = 20.sp
            )

            // Navigate to Quiz Category Selection
            Button(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                onClick = {
                    navController.navigate(NavRoutes.SELECT_QUIZ_CATEGORY)
                }
            ) { Text("Start a Quiz") }

            // Navigate to Quiz Records Screen
            Button(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                onClick = {
                    navController.navigate(NavRoutes.QUIZ_RECORDS)
                }
            ) {
                Text("View Quizzes Record")
            }


            // Logout
            Button(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                onClick = {
                    userViewModel.logout()
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            ) { Text("Log Out") }
        }
    }
}

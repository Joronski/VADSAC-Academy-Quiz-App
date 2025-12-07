package com.example.finalscasestudy.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalscasestudy.ui.commons.ErrorDialog
import com.example.finalscasestudy.ui.nav.NavRoutes
import com.example.finalscasestudy.ui.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel) {

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Reset errors when entering screen
    LaunchedEffect(Unit) {
        userViewModel.resetErrors()
    }

    LaunchedEffect(Unit) {
        userViewModel.registerResult.collectLatest { result ->
            if (result == "SUCCESS") {
                navController.navigate(NavRoutes.LOGIN) {
                    popUpTo(NavRoutes.REGISTER) { inclusive = true }
                }
            } else if (!result.isNullOrEmpty()) {
                errorMessage = result
                showErrorDialog = true
            }
        }
    }

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

            Card(
                modifier = Modifier.padding(20.dp).fillMaxWidth().fillMaxHeight(0.75f),
                shape = RoundedCornerShape(15.dp),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Register", fontSize = 25.sp, fontWeight = FontWeight.SemiBold)

                    TextField(
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") }
                    )

                    TextField(
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") }
                    )

                    TextField(
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    TextField(
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Button(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        onClick = { userViewModel.register(username, email, password, confirmPassword) }
                    ) {
                        Text("Register Account")
                    }

                    Text(
                        text = "Already have an account? Log in Here",
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            navController.navigate(NavRoutes.LOGIN)
                        }
                    )
                }
            }
        }
    }

    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = {
                showErrorDialog = false
                userViewModel.resetErrors() // <- Reset the flow value
            }
        )
    }
}

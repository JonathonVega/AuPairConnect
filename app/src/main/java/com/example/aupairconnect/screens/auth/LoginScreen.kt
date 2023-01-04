package com.example.aupairconnect

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    onNavigation: NavHostController,
    viewModel: AuthViewModel
) {
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }

    LaunchedEffect(viewModel.userNotConfirmedFailure){
        println("userNotConfirmedFailure is: ${viewModel.userNotConfirmedFailure.value}")

    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Login Page", fontSize = 40.sp)

        //TODO Put TextFields into a box
        Box(){
            Column() {
                OutlinedTextField(value = emailValue.value,
                    onValueChange = {emailValue.value = it},
                    label = { Text(text = "Enter Email")})
                OutlinedTextField(value = passwordValue.value,
                    onValueChange = {passwordValue.value = it},
                    label = {Text(text = "Enter Password")})
            }
        }

        Button(onClick = {
            if(emailValue.value.isNotEmpty() && passwordValue.value.isNotEmpty()){
                viewModel.signIn(emailValue.value, passwordValue.value)
            } else{
                //TODO: Add warning text when sign in failed
                println("The email and/or password is blank")
            }
        }){
            Text(text = "Click to Sign-in")
        }
        Button(onClick = {
            viewModel.getCurrentUser()
        }){ Text(text = "Check Current User")}
        Button(onClick = {
            val route = Screens.RegisterScreen.route
            onNavigation.navigate(route)
        }){ Text(text = "Register Here!")}
    }
}

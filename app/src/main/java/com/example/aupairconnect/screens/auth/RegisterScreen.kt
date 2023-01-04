package com.example.aupairconnect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun RegisterScreen(
    onNavigation: NavHostController,
    viewModel: AuthViewModel
){

    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val passwordConfirmationValue = remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Verify Page", fontSize = 40.sp)

        //TODO Put TextFields into a box
        Box(){
            Column() {
                OutlinedTextField(value = emailValue.value,
                    onValueChange = {emailValue.value = it},
                    label = { Text(text = "Enter Email")})
                OutlinedTextField(value = passwordValue.value,
                    onValueChange = {passwordValue.value = it},
                    label = {Text(text = "Enter Password")})
                OutlinedTextField(value = passwordConfirmationValue.value,
                    onValueChange = {passwordConfirmationValue.value = it},
                    label = {Text(text = "Confirm Password")})
            }
        }

        Button(onClick = {
            if(passwordValue.value == passwordConfirmationValue.value){
                println("password confirmed")
                viewModel.registerUser(emailValue.value, passwordValue.value)

                val route = Screens.ConfirmRegisterScreen.route
                onNavigation.navigate(route)
            }
        }){
            Text(text = "Sign Up")
        }
        Button(onClick = {

            val route = Screens.LoginScreen.route
            onNavigation.navigate(route)


        }){ Text(text = "Go Back to login")}
    }
}
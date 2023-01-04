package com.example.aupairconnect.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.aupairconnect.AuthViewModel
import com.example.aupairconnect.graphs.Graph

@Composable
fun ConfirmRegisterScreen(
    onNavigation: NavHostController,
    viewModel: AuthViewModel
) {
    val codeValue = remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Register Page", fontSize = 40.sp)

        Box(){
            Column() {
                OutlinedTextField(value = codeValue.value,
                    onValueChange = {codeValue.value = it},
                    label = { Text(text = "Enter Verification Code") })
            }
        }

        Button(onClick = {
            if(codeValue.value.length == 6){
                viewModel.confirmSignUp(codeValue.value)
                onNavigation.popBackStack()
                onNavigation.navigate(Graph.HOME)
            }
        }){
            Text(text = "Verify")
        }
        Button(onClick = {
            viewModel.resendVerificationCode()
        }){
            Text(text = "Resend Code")
        }
    }
}

package com.example.aupairconnect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aupairconnect.graphs.Graph
import com.example.aupairconnect.presentation.ui.theme.ACTheme
import com.example.aupairconnect.presentation.auth.AuthViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onNavigation: NavHostController,
    viewModel: AuthViewModel
) {
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    var passwordVisibility = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel.userNotConfirmedFailure){
        println("userNotConfirmedFailure is: ${viewModel.userNotConfirmedFailure.value}")

    }

    Column(modifier = Modifier
        .clickable { keyboardController?.hide() }
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Aupair Connect", fontSize = 40.sp, modifier = Modifier.padding(bottom = 40.dp))
            TextField(modifier = Modifier.padding(vertical = 10.dp),
                value = emailValue.value,
                onValueChange = {emailValue.value = it},
                label = { Text(text = "Enter Email")},
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White))
            TextField(modifier = Modifier.padding(vertical = 10.dp),
                value = passwordValue.value,
                onValueChange = {passwordValue.value = it},
                label = {Text(text = "Enter Password")},
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if(passwordVisibility.value)
                        ImageVector.vectorResource(id = R.drawable.baseline_visibility_24)
                    else
                        ImageVector.vectorResource(id = R.drawable.baseline_visibility_off_24)
                    val description = if (passwordVisibility.value) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisibility.value = !passwordVisibility.value}){
                        Icon(imageVector  = image, description)
                    }
                }
            )
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),onClick = {
                if(emailValue.value.isNotEmpty() && passwordValue.value.isNotEmpty()){
                    viewModel.signIn(emailValue.value, passwordValue.value)
                } else{
                    //TODO: Add warning text when sign in failed
                    println("The email and/or password is blank")
                }
            }){
                Text(text = "Sign In", modifier = Modifier.padding(vertical = 5.dp))
            }
            Row(modifier = Modifier.padding(top = 20.dp)){
                Text(text = "Forgot Password? ")
                // TODO: Add function to reset password
                ClickableText(text = AnnotatedString("Reset here"), onClick = {}, style = TextStyle(fontSize = 16.sp, color = ACTheme))
            }
        }

        Button(onClick = {
            viewModel.getCurrentUser()
        }){ Text(text = "Check Current User")}
        Button(onClick = {
            onNavigation.navigate(Graph.HOME)
        }){
            Text(text = "Guest Sign In")
        }
        Row(modifier = Modifier.padding(top = 50.dp)){
            Text(text = "Don't have an account? ")
            ClickableText(text = AnnotatedString("Sign up"), onClick = {viewModel.goToSignUpScreen()}, style = TextStyle(fontSize = 16.sp, color = ACTheme))
        }
    }
}

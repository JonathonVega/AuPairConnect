package com.example.aupairconnect

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aupairconnect.presentation.ui.theme.ACTheme
import com.example.aupairconnect.presentation.auth.AuthViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RegisterScreen(
    onNavigation: NavHostController,
    viewModel: AuthViewModel
){
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val passwordConfirmationValue = remember { mutableStateOf("") }

    val nameValue = remember { mutableStateOf("") }
    val ageValue = remember { mutableStateOf("") }
    val originValue = remember { mutableStateOf("") }
    val currentLocationValue = remember { mutableStateOf("") }

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    //TODO: Add loading spinner when getting current location

    HorizontalPager(count = 4,
        state = pagerState,
        userScrollEnabled = false,
        modifier = Modifier.fillMaxSize()) { page ->
        when(page){
            0 -> {
                Column(
                    modifier = Modifier.width(280.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Sign Up", fontSize = 40.sp)

                    TextField(value = emailValue.value,
                        onValueChange = {emailValue.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        label = { Text(text = "Enter Email")})
                    TextField(value = passwordValue.value,
                        onValueChange = {passwordValue.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        label = {Text(text = "Enter Password")})
                    TextField(value = passwordConfirmationValue.value,
                        onValueChange = {passwordConfirmationValue.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        label = {Text(text = "Confirm Password")})

                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }){
                        Text(text = "Continue")
                    }


                    //TODO Programmatically go back instead of loading new screen
                    Row(modifier = Modifier.padding(top = 50.dp)){
                        Text(text = "Already have an account? ")
                        ClickableText(text = AnnotatedString("Sign in"), onClick = {viewModel.goToSignInScreen()}, style = TextStyle(fontSize = 16.sp, color = ACTheme))
                    }
                }
            }
            1 -> {
                Column(modifier = Modifier.padding(start = 75.dp, end = 75.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    profileRegisterImage()
                    TextField(value = nameValue.value,
                        onValueChange = {nameValue.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        label = { Text(text = "Enter Your Name")})
                    TextField(value = ageValue.value,
                        onValueChange = {ageValue.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        label = {Text(text = "What is your age?")})
                    TextField(value = originValue.value,
                        onValueChange = { originValue.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        label = {Text(text = "Where Are You From?")})
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 0.dp)) {
                        TextField(modifier = Modifier
                            .weight(3f)
                            .padding(end = 15.dp),
                            value = viewModel.userLocation.value,
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                            onValueChange = {currentLocationValue.value = it},
                            enabled = false,
                            label = {Text(text = "Current Location?")},
                            trailingIcon = {
                                val image = Icons.Filled.Place
                                IconButton(modifier = Modifier.size(50.dp),
                                    onClick = {viewModel.getCurrentLocation(context)}){
                                    androidx.compose.material.Icon(imageVector = image, "Current Location", tint = ACTheme)
                                }
                            })
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Button(onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }){
                            Text(text = "Back")
                        }
                        Button(onClick = {
                            if(passwordValue.value == passwordConfirmationValue.value){
                                println("password confirmed")
                                //TODO: Put call to AWS into viewModel
                                viewModel.registerUser(emailValue.value, passwordValue.value)

                                val route = Screens.VerifyScreen.route
                                onNavigation.navigate(route)
                            }
                        }){
                            Text(text = "Sign Up")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun profileRegisterImage(){
    androidx.compose.foundation.Image(
        painter = painterResource(id = R.drawable.baseline_person_24),
        contentDescription = "Image",
        modifier = Modifier
            .border(border = BorderStroke(1.dp, color = Color.Black), shape = CircleShape)
            .width(100.dp)
            .height(100.dp)
            .clip(shape = CircleShape))
}

@Composable
fun registerAuth(){

}

@Composable
fun registerInformation(){

}



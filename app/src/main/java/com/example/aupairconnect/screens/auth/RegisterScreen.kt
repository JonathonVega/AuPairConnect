package com.example.aupairconnect

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Register Page", fontSize = 40.sp)

                    OutlinedTextField(value = emailValue.value,
                        onValueChange = {emailValue.value = it},
                        label = { Text(text = "Enter Email")})
                    OutlinedTextField(value = passwordValue.value,
                        onValueChange = {passwordValue.value = it},
                        label = {Text(text = "Enter Password")})
                    OutlinedTextField(value = passwordConfirmationValue.value,
                        onValueChange = {passwordConfirmationValue.value = it},
                        label = {Text(text = "Confirm Password")})
                    Button(onClick = {
                        //TODO Programmatically go back instead of loading new screen
                        val route = Screens.LoginScreen.route
                        onNavigation.navigate(route)
                    }){ Text(text = "Go Back to login")}
                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }){
                        Text(text = "Continue")
                    }
                }
            }
            1 -> {
                Column() {
                    OutlinedTextField(value = nameValue.value,
                        onValueChange = {nameValue.value = it},
                        label = { Text(text = "Enter Your Name")})
                    OutlinedTextField(value = ageValue.value,
                        onValueChange = {ageValue.value = it},
                        label = {Text(text = "What is your age?")})

                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }){
                        Text(text = "Back")
                    }
                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }){
                        Text(text = "Continue")
                    }
                }
            }
            2 -> {
                Column(modifier = Modifier.padding(start = 50.dp, end = 50.dp)) {
                    OutlinedTextField(value = originValue.value,
                        onValueChange = { originValue.value = it},
                        label = {Text(text = "Where Are You From?")})
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 0.dp)) {
                        TextField(modifier = Modifier.weight(3f).padding(end = 15.dp),
                            value = viewModel.userLocation.value,
                            onValueChange = {currentLocationValue.value = it},
                            enabled = false,
                            label = {Text(text = "Current Location?")})
//                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
//                            Button(modifier = Modifier
//                                .size(40.dp),
//                                onClick = {
//
//                                }){
////                            Text(text = "Get Location")
//                                Icon(Icons.Default.Place, contentDescription = "Location", tint = Color.White, modifier = Modifier.fillMaxSize())
//                            }
//                        }
                        Button(modifier = Modifier
                            .size(50.dp),
                            contentPadding = PaddingValues(10.dp),
                            onClick = {
                                viewModel.getCurrentLocation(context)
                            }){
                            Icon(Icons.Default.Place, contentDescription = "Location", tint = Color.White)
                        }
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
                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }){
                        Text(text = "Back")
                    }
                }
            }
        }
    }
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .verticalScroll(rememberScrollState()),
//        verticalArrangement = Arrangement.SpaceBetween,
//        horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "Register Page", fontSize = 40.sp)
//
//        //TODO Put TextFields into a box
//        Box(){
//
//
//        }
//
//
//    }
}

@Composable
fun registerAuth(){

}

@Composable
fun registerInformation(){

}


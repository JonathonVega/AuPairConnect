package com.example.aupairconnect.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, route: String){
    
    LaunchedEffect(key1 = true){
        delay(2500)
        navController.navigate(route)
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
//        Image(painter = painter)
        Image(painter = painterResource(id = com.example.aupairconnect.R.drawable.stitch), "Splash Screen")
    }
    
}
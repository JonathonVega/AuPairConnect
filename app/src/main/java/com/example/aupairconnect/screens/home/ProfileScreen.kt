package com.example.aupairconnect

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(
    onNavigation: NavHostController,
    viewModel: AuthViewModel
){

    //TODO: logoutState seems useless. Read later and delete
    val logoutState = remember{ mutableStateOf(false) }
    Column() {
        Text(text = "This is the Profile Screen")
        Button(onClick = {
            viewModel.signOut()
            logoutState.value = true
        }) {
            Text(text = "Log out")
            if(logoutState.value){
                logOut()
            }
        }
    }
}

@Composable
fun logOut(){
    val activity = (LocalContext.current as? Activity)
    activity?.finish()
    activity?.startActivity(Intent(activity, MainActivity::class.java))
}
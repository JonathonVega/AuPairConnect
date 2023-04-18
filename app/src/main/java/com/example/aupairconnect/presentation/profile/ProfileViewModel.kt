package com.example.aupairconnect.presentation.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.aupairconnect.MainActivity
import com.example.aupairconnect.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository
) : ViewModel() {

    init {

    }

    fun signOut(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            authRepository.signOut()
            val activity = (context as? Activity)
            activity?.finish()
            activity?.startActivity(Intent(activity, MainActivity::class.java))
        }

    }

    fun getCurrentUser(){
        CoroutineScope(Dispatchers.IO).launch {
            authRepository.getCurrentUser()
        }

    }
}
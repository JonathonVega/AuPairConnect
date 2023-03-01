package com.example.aupairconnect.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.aupairconnect.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository
) : ViewModel() {


    fun signOut(){
        CoroutineScope(Dispatchers.IO).launch {
            authRepository.signOut()
        }

    }
}
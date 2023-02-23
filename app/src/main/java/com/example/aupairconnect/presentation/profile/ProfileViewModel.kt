package com.example.aupairconnect.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.aupairconnect.repositories.AuthRepository

class ProfileViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository
) : ViewModel() {


    fun signOut(){
        authRepository.signOut()
    }
}
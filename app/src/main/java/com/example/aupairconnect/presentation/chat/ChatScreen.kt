package com.example.aupairconnect

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.aupairconnect.presentation.chat.ChatViewModel

@Composable
fun ChatScreen(
    onNavigation: NavHostController,
    viewModel: ChatViewModel
){
    Text(text = "This is the Chat Screen")
}
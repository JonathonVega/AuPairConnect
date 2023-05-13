package com.example.aupairconnect

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(
    val route:String,
){
    object LoginScreen: Screens("login")
    object RegisterScreen: Screens("register")
    object VerifyScreen: Screens("verify")
    object HomeScreen: Screens("home")
    object ChatScreen: Screens("chat")
    object ProfileScreen: Screens("profile")
    object EditProfileScreen: Screens("editProfile")
    object HomeTabs: Screens("homeTabs")
}

sealed class BottomNavigationItem(val route: String, val label: String, val icon: ImageVector){
    object Home: BottomNavigationItem("home", "Home", Icons.Default.Home)
    object Discover: BottomNavigationItem("discover", "Discover", Icons.Default.Home)
    object Chat: BottomNavigationItem("chat", "Chat", Icons.Default.Email)
    object Profile: BottomNavigationItem("profile", "Profile", Icons.Default.Person)
}
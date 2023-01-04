package com.example.aupairconnect.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aupairconnect.*

@Composable
fun HomeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination =  BottomNavigationItem.Discover.route
    ){
        val authRepository = AuthRepository()
        composable(route = BottomNavigationItem.Discover.route){
            DiscoverScreen()
        }
        composable(route = BottomNavigationItem.Chat.route){
            ChatScreen()
        }
        composable(route = BottomNavigationItem.Profile.route){
            val authViewModel = AuthViewModel(navController, authRepository)
            ProfileScreen(navController, authViewModel)
        }
    }
}
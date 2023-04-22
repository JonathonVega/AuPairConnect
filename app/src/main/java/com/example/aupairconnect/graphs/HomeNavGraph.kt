package com.example.aupairconnect.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aupairconnect.*
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.presentation.chat.ChatViewModel
import com.example.aupairconnect.presentation.discover.DiscoverViewModel
import com.example.aupairconnect.presentation.profile.ProfileViewModel
import com.example.aupairconnect.repositories.DatastoreRepository

@Composable
fun HomeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination =  BottomNavigationItem.Discover.route
    ){
        val authRepository = AuthRepository()
        val datastoreRepository = DatastoreRepository()
        composable(route = BottomNavigationItem.Discover.route){
            val discoverViewModel = DiscoverViewModel(navController)
            DiscoverScreen(navController, discoverViewModel)
        }
        composable(route = BottomNavigationItem.Chat.route){
            val chatViewModel = ChatViewModel(navController)
            ChatScreen(navController, chatViewModel)
        }
        composable(route = BottomNavigationItem.Profile.route){
            val profileViewModel = ProfileViewModel(navController, authRepository, datastoreRepository)
            ProfileScreen(navController, profileViewModel)
        }
    }
}
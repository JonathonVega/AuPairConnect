package com.example.aupairconnect

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.aupairconnect.graphs.Graph
import com.example.aupairconnect.screens.VerifyScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screens.LoginScreen.route
    ){

        val authRepository = AuthRepository()
        val authViewModel = AuthViewModel(navController, authRepository)

        composable(route = Screens.LoginScreen.route){
            LoginScreen(onNavigation = navController, viewModel = authViewModel)
        }
        composable(route = Screens.RegisterScreen.route){
            RegisterScreen(onNavigation = navController, viewModel = authViewModel)
        }
        composable(route = Screens.VerifyScreen.route){
            VerifyScreen(onNavigation = navController, viewModel = authViewModel)
        }

    }
}
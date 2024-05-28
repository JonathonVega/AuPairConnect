package com.example.aupairconnect.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aupairconnect.HomeScreen
import com.example.aupairconnect.StoreUserEmail
import com.example.aupairconnect.authNavGraph
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.presentation.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RootNavigationGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {

        authNavGraph(navController = navController)
        composable(route = Graph.HOME) {
            val datastore = StoreUserEmail(LocalContext.current)
            val savedEmail = datastore.getEmail.collectAsState(initial = "")



            println("Do we come here???????/")
            println(savedEmail.value)
            HomeScreen(savedEmail.value)
        }
    }
}

object Graph{
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
}


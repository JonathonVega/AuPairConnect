package com.example.aupairconnect

import android.util.Log
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.core.Amplify
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.graphs.HomeNavGraph
import com.example.aupairconnect.presentation.ui.theme.ACTheme
import com.example.aupairconnect.repositories.APIRepository
import com.example.aupairconnect.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()){

//    val authRepository = AuthRepository()
//    val apiRepository = APIRepository()
//    var userData = User()
//
//    CoroutineScope(Dispatchers.IO).launch {
//        val userId = authRepository.getUserId()
//        userData = apiRepository.getUserById(userId)
//    }
//    val coroutineScope = rememberCoroutineScope()
//    coroutineScope.launch(Dispatchers.IO) {
//
//    }

    HomeNavGraph(navController = navController)
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavigationItem.Discover,
        BottomNavigationItem.Chat,
        BottomNavigationItem.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation(backgroundColor = ACTheme) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavigationItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.label)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}
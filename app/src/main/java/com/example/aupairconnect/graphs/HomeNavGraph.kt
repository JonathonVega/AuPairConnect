package com.example.aupairconnect.graphs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amplifyframework.kotlin.auth.Auth
import com.example.aupairconnect.*
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.presentation.chat.ChatViewModel
import com.example.aupairconnect.presentation.discover.AupairCardScreen
import com.example.aupairconnect.presentation.discover.DiscoverViewModel
import com.example.aupairconnect.presentation.profile.EditProfileScreen
import com.example.aupairconnect.presentation.profile.ProfileViewModel
import com.example.aupairconnect.repositories.APIRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination =  Screens.HomeTabs.route
    ){

        val authRepository = AuthRepository()
        val apiRepository = APIRepository()
        val discoverViewModel = DiscoverViewModel(navController)
        val chatViewModel = ChatViewModel(navController)
        val profileViewModel = ProfileViewModel(navController, authRepository)

        val tabIndex = mutableStateOf(0)
        var userData = User()

        CoroutineScope(Dispatchers.IO).launch {
            val userId = AuthRepository.getUserId()
            userData = APIRepository.getUserById(userId)

            //turn User object to Aupair object
//            userAupairData =

            profileViewModel.insertUserDataToViewModel(userData)
            discoverViewModel.userAupair = userData
        }

        composable(route = Screens.HomeTabs.route){


//            profileViewModel.profileName.value = userData!!.name.toString()
//            profileViewModel.userData = userData
            val tabs = listOf("Discover", "Chat", "Profile")

            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = tabIndex.value) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex.value == index,
                            onClick = { tabIndex.value = index }
                        )
                    }
                }
                when (tabIndex.value) {
                    0 -> {
                        DiscoverScreen(navController, discoverViewModel)
                    }
                    1 -> {
                        ChatScreen(navController, chatViewModel)
                    }
                    2 -> {
                        ProfileScreen(navController, profileViewModel)
                    }
                }
            }
        }
        composable(route = Screens.EditProfileScreen.route){
            val datastore = StoreUserEmail(LocalContext.current)
            val savedEmail = datastore.getEmail.collectAsState(initial = "")
            EditProfileScreen(navController, viewModel = profileViewModel, userData!!, savedEmail.value)
        }
        composable(route = Screens.AupairCardScreen.route){
            AupairCardScreen(navController, discoverViewModel)
        }

//        composable(route = BottomNavigationItem.Chat.route){
//            val chatViewModel = ChatViewModel(navController)
//            ChatScreen(navController, chatViewModel)
//        }
//        composable(route = BottomNavigationItem.Profile.route){
//            val profileViewModel = ProfileViewModel(navController, authRepository, datastoreRepository)
//            ProfileScreen(navController, profileViewModel)
//        }
    }
}







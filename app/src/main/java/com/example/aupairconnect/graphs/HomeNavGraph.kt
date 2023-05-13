package com.example.aupairconnect.graphs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amplifyframework.kotlin.core.Amplify
import com.example.aupairconnect.*
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.presentation.chat.ChatViewModel
import com.example.aupairconnect.presentation.discover.DiscoverViewModel
import com.example.aupairconnect.presentation.profile.EditProfileScreen
import com.example.aupairconnect.presentation.profile.ProfileViewModel
import com.example.aupairconnect.repositories.DatastoreRepository
import kotlinx.coroutines.runBlocking


@Composable
fun HomeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination =  Screens.HomeTabs.route
    ){


        val authRepository = AuthRepository()
        val datastoreRepository = DatastoreRepository()
        val discoverViewModel = DiscoverViewModel(navController)
        val chatViewModel = ChatViewModel(navController)
        val profileViewModel = ProfileViewModel(navController, authRepository, datastoreRepository)
        val tabIndex = mutableStateOf(0)
        var userData:User? = null

        composable(route = Screens.HomeTabs.route){

            val datastore = StoreUserEmail(LocalContext.current)
            val savedEmail = datastore.getEmail.collectAsState(initial = "")
            profileViewModel.userEmail.value = savedEmail.value
            println("We Come back again!!!!!!")
            userData = getUserData(datastoreRepository, savedEmail.value)
            profileViewModel.profileName.value = userData!!.name.toString()
            profileViewModel.userData = userData
            val tabs = listOf("Meet", "Chat", "Profile")

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
                        ProfileScreen(navController, profileViewModel, savedEmail.value, userData!!)
                    }
                }
            }
        }
        composable(route = Screens.EditProfileScreen.route){
            val datastore = StoreUserEmail(LocalContext.current)
            val savedEmail = datastore.getEmail.collectAsState(initial = "")
            val profileViewModel = ProfileViewModel(navController, authRepository, datastoreRepository)
            EditProfileScreen(navController, viewModel = profileViewModel, userData!!, savedEmail.value)
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


fun getUserData(datastoreRepository:DatastoreRepository, email: String): com.example.aupairconnect.domain.model.User = runBlocking{
    var dataInfo: com.example.aupairconnect.domain.model.User = com.example.aupairconnect.domain.model.User()
    dataInfo = datastoreRepository.getUserData(email)

    println("Should hit this last")
    val trueDataInfo = dataInfo
    if (trueDataInfo != null) {
        trueDataInfo
    }
    trueDataInfo
}




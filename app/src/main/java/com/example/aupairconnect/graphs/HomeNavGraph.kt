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
import com.amplifyframework.kotlin.core.Amplify
import com.example.aupairconnect.*
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.presentation.chat.ChatViewModel
import com.example.aupairconnect.presentation.discover.DiscoverViewModel
import com.example.aupairconnect.presentation.profile.ProfileViewModel
import com.example.aupairconnect.repositories.DatastoreRepository
import kotlinx.coroutines.runBlocking

//TODO: Should probably delete the navGraph idea and stick with the Jetpack Compose Tabs
@Composable
fun HomeNavGraph(navController: NavHostController){
    val tabIndex = remember{ mutableStateOf(0) }
    val datastore = StoreUserEmail(LocalContext.current)
    val savedEmail = datastore.getEmail.collectAsState(initial = "")

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
        val authRepository = AuthRepository()
        val datastoreRepository = DatastoreRepository()
        val userData = getUserData(datastoreRepository, savedEmail.value)
        when (tabIndex.value) {
            0 -> {
                val discoverViewModel = DiscoverViewModel(navController)
                DiscoverScreen(navController, discoverViewModel)
            }
            1 -> {
                val chatViewModel = ChatViewModel(navController)
                ChatScreen(navController, chatViewModel)
            }
            2 -> {
                val profileViewModel = ProfileViewModel(navController, authRepository, datastoreRepository, savedEmail.value)
                ProfileScreen(navController, profileViewModel, savedEmail.value, userData)
            }
        }
    }

    //TODO: Delete code for NavHost Later once removing all of Navigation Graph
//    NavHost(
//        navController = navController,
//        route = Graph.HOME,
//        startDestination =  BottomNavigationItem.Discover.route
//    ){
//        var tabIndex by remember { mutableStateOf(0) }



//        val authRepository = AuthRepository()
//        val datastoreRepository = DatastoreRepository()
//        composable(route = BottomNavigationItem.Discover.route){
//            val discoverViewModel = DiscoverViewModel(navController)
//            DiscoverScreen(navController, discoverViewModel)
//        }
//        composable(route = BottomNavigationItem.Chat.route){
//            val chatViewModel = ChatViewModel(navController)
//            ChatScreen(navController, chatViewModel)
//        }
//        composable(route = BottomNavigationItem.Profile.route){
//            val profileViewModel = ProfileViewModel(navController, authRepository, datastoreRepository)
//            ProfileScreen(navController, profileViewModel)
//        }
//    }
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

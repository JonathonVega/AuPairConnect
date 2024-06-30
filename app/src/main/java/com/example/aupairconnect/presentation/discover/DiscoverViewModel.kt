package com.example.aupairconnect.presentation.discover

import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.aupairconnect.domain.model.Aupair
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.repositories.APIRepository
import com.example.aupairconnect.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscoverViewModel constructor(
    private val onNavigation: NavHostController
) : ViewModel() {
    var aupairList: MutableState<List<Aupair>> = mutableStateOf(ArrayList())

//    val refreshScope = rememberCoroutineScope()
    var refreshing = mutableStateOf(false)

    var signInSuccessful = mutableStateOf(false)

    var userAupair: User = User()
    var selectedAupair = Aupair()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            aupairList.value = APIRepository.getListOfNearbyAupairs(userAupair)
        }
//        if (userAupair.userId != null) {
//            CoroutineScope(Dispatchers.IO).launch {
//                delay(2000)
//                APIRepository.getListOfNearbyAupairs(userAupair)
//            }
//        }
//        else {
//            CoroutineScope(Dispatchers.IO).launch {
//                APIRepository.getListOfNearbyAupairs(AuthRepository.getUserId())
//            }
//        }


    }


}
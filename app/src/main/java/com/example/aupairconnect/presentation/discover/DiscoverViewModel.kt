package com.example.aupairconnect.presentation.discover

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.aupairconnect.domain.model.Aupair

class DiscoverViewModel constructor(
    private val onNavigation: NavHostController
) : ViewModel() {
    val aupairList: MutableState<List<Aupair>> = mutableStateOf(ArrayList())

    init {
        val aupairBio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        // TODO: Dummy Data - Delete after building backend.
        val aupair1 = Aupair("Shirley",
            23,
            "Bolivia",
            "New York City, NY, United States of America",
            false,
            bio = aupairBio)
        val aupair2 = Aupair("Jessica",
            26,
            "Japan",
            "New York City, NY, United States of America",
            false)
        val aupair3 = Aupair("Sam",
            22,
            "United Kingdom",
            "New York City, NY, United States of America",
            false)
        val aupair4 = Aupair("Manny",
            25,
            "Mexico",
            "New York City, NY, United States of America",
            true)
        val aupair5 = Aupair("Berry",
            19,
            "Australia",
            "New York City, NY, United States of America",
            false)
        aupairList.value = listOf(aupair1, aupair2, aupair3, aupair4, aupair5)
    }
}
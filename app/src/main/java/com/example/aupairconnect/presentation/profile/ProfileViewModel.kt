package com.example.aupairconnect.presentation.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.aupairconnect.MainActivity
import com.example.aupairconnect.StoreUserEmail
import com.example.aupairconnect.domain.model.User
//import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository
) : ViewModel() {

    val userEmail = mutableStateOf("")
    val profileName = mutableStateOf("")
    val profileStatus = mutableStateOf("")
    val profileNationality = mutableStateOf("")
    val profileAge = mutableStateOf("")
    val profileCurrentLocation = mutableStateOf("")
    val profileBio = mutableStateOf("")

    var userProfileData = User()

//    val editStatus = mutableStateOf("")
//    val editNationality = mutableStateOf("")
//    lateinit var userstuff: com.example.aupairconnect.domain.model.User

    val COUNTRY_LIST = arrayOf("Afghanistan","Albania","Algeria","Andorra","Angola","Anguilla","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas",
        "Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","British Virgin Islands","Brunei",
        "Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Colombia","Comoros","Congo","Cook Islands"
        ,"Costa Rica","Cote D Ivoire","Croatia","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea",
        "Estonia","Ethiopia","Falkland Islands","Faroe Islands","Fiji","Finland","France","French Polynesia","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada",
        "Guam","Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy","Jamaica","Japan",
        "Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Madagascar",
        "Malawi","Malaysia","Maldives","Mali","Malta","Mauritania","Mauritius","Mexico","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nepal","Netherlands",
        "Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland",
        "Portugal","Puerto Rico","Qatar","Romania","Russia","Rwanda","Saint Kitts and Nevis","Saint Lucia","Saint Vincent","Samoa","San Marino","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone",
        "Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland",
        "Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor-Leste","Togo","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Uganda","Ukraine","United Arab Emirates",
        "United Kingdom","United States","Uruguay","Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)","Yemen","Zambia","Zimbabwe")

//    suspend fun getUserData(email: String): com.example.aupairconnect.domain.model.User{
//
//        var dataInfo: com.example.aupairconnect.domain.model.User = com.example.aupairconnect.domain.model.User()
//        runBlocking {
//            dataInfo = datastoreRepository.getUserData(email)
//            delay(2500)
//        }
//
//
//        val trueDataInfo = dataInfo
////    if (trueDataInfo != null) {
////        trueDataInfo
////    }
////    trueDataInfo
//        return dataInfo
//    }

    fun signOut(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val datastore = StoreUserEmail(context)
            datastore.saveEmail("")
            authRepository.signOut()
            val activity = (context as? Activity)
            activity?.finish()
            activity?.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    fun insertUserDataToViewModel(userData: com.example.aupairconnect.domain.model.User){
        profileName.value = userData.name.toString()
        profileStatus.value = userData.status.toString()
        profileAge.value = userData.getUserAge().toString()
        profileNationality.value = userData.nationality.toString()
        profileCurrentLocation.value = userData.latitude.toString()
        profileBio.value = userData.bio.toString()
        userProfileData = userData
    }
}
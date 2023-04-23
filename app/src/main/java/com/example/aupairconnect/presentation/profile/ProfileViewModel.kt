package com.example.aupairconnect.presentation.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.kotlin.core.Amplify
import com.example.aupairconnect.MainActivity
import com.example.aupairconnect.StoreUserEmail
//import com.example.aupairconnect.domain.model.User
import com.amplifyframework.datastore.generated.model.User
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.repositories.DatastoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository,
    private val userEmail: String
) : ViewModel() {

//    var userEmail:String? = null
    var userInfo: com.example.aupairconnect.domain.model.User? = null
    var userName = mutableStateOf("")
    var nationality = mutableStateOf("")
    var age = mutableStateOf("")

    init {
        println("We will get the userdata from userEmail $userEmail!!!!!!!!")
//        var userInfo: com.example.aupairconnect.domain.model.User?
        val listOfUsers = mutableListOf<User>();
        CoroutineScope(Dispatchers.IO).launch {
            Amplify.DataStore.query(User::class, Where.matches(User.EMAIL.eq(userEmail)))
                .catch { Log.e("MyAmplifyApp", "Query failed", it) }
                .collect {
                    Log.i("MyAmplifyApp", "User email in viewmodel: ${it.email}")
                    val userData = com.example.aupairconnect.domain.model.User(it.name, it.age, it.nationality, it.currentLocation, it.status, it.bio)
                    listOfUsers.add(it)
                    userName.value = listOfUsers[0].name
                    nationality.value = listOfUsers[0].nationality
                    age.value = listOfUsers[0].age.toString()
                    println("Now printing mutable ${userName.value}")
//                    println("Now printing ${userInfo?.name}")
                }
        }
        println("Did we get the userdata from viewmodel!!!!!!!****!")
    }

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

    fun getUserData(email: String) {
        println("We will get the userdata from viewmodel!!!!!!!!")
//        var userInfo: com.example.aupairconnect.domain.model.User?
        val listOfUsers = mutableListOf<User>();
        CoroutineScope(Dispatchers.IO).launch {
            Amplify.DataStore.query(User::class, Where.matches(User.EMAIL.eq(email)))
                .catch { Log.e("MyAmplifyApp", "Query failed", it) }
                .collect {
                    Log.i("MyAmplifyApp", "User email in viewmodel: ${it.email}")
                    val userData = com.example.aupairconnect.domain.model.User(it.name, it.age, it.nationality, it.currentLocation, it.status, it.bio)
//                    userInfo = userData
//                    userName.value = it.name
                    listOfUsers.add(it)
                    userName.value = listOfUsers[0].name
                    println("Now printing mutable ${userName.value}")
//                    println("Now printing ${userInfo?.name}")
                }
        }
        println("Did we get the userdata from viewmodel!!!!!!!****!")
    }
}
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
}
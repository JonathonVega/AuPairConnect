package com.example.aupairconnect

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.exceptions.service.UserNotConfirmedException
import com.amplifyframework.core.Amplify
import com.example.aupairconnect.graphs.Graph
import kotlinx.coroutines.*

class AuthViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository
) : ViewModel() {

    var signInSuccessful = mutableStateOf(false)
    var userNotConfirmedFailure = mutableStateOf(false)
    var userEmail: String = ""


    fun getCurrentUser(): Unit{
        return Amplify.Auth.getCurrentUser({Log.i("AuthCurrentUser", "Current User is: $it")},{Log.e ("AuthCurrentUser", "Couldn't retrieve current user: ", it) })
    }

    fun signIn(email:String, password:String) {
        println("In viewModel")

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO){
                val result = Amplify.Auth.signIn(email, password,
                    { result ->
                        if (result.isSignedIn) {
                            Log.i("AuthQuickstart", "Sign in succeeded")
                            signInSuccessful.value = true
                        } else {
                            Log.i("AuthQuickstart", "Sign in not complete")
                            signInSuccessful.value = false
                        }
                    },
                    {error ->
                        when(error){
                            is UserNotConfirmedException -> {
                                Log.i("UserNotConfirmedException", error.toString())
                                userNotConfirmedFailure.value = true
                            }
                            is AuthException -> {
                                Log.e("AuthQuickstart", error.toString())
                            }
                        }
                    }
                )
                println("Finished auth, onto next screen")
                delay(2000)
                withContext(Dispatchers.Main){
                    if(signInSuccessful.value){
                        onNavigation.popBackStack()
                        onNavigation.navigate(Graph.HOME)
                    }
                    if(userNotConfirmedFailure.value){
                        println("It pushes to verify screen")
                        userEmail = email
                        val route = Screens.ConfirmRegisterScreen.route
                        onNavigation.navigate(route)
                    }
                }
            }
        }
    }

    fun registerUser(email: String, password: String){
        authRepository.registerUser(email, password)
    }

    fun confirmSignUp(code:String){
        Amplify.Auth.confirmSignUp(userEmail, code,
            { result ->
                if(result.isSignUpComplete){
                    Log.i("Auth Confirm Signup", "Confirmation Succeeded")
                }
        },
            {Log.e("Auth Confirm SignUp", "Confirmation Failed")}
        )
    }

    fun signOut(){
        authRepository.signOut()
    }

    fun resendVerificationCode(){
        Amplify.Auth.resendSignUpCode(userEmail,
        { Log.i("AuthDemo", "Code was sent again: $it") }, {Log.e("AuthDemo", "Failed to resend code", it) })
    }
}
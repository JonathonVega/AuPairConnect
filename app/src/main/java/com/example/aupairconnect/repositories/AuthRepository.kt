package com.example.aupairconnect.repositories

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.generated.model.User
//import com.amplifyframework.core.Amplify
import com.amplifyframework.kotlin.core.Amplify
import com.example.aupairconnect.graphs.Graph
import java.util.Date


//TODO: Comment File entirely. May Return to it another time in the future
class AuthRepository() {

    suspend fun getCurrentUser(){
        try {
            val result = Amplify.Auth.getCurrentUser()
            Log.i("AuthQuickstart", "Current user details are: $result")
        } catch (error: Exception) {
            Log.e("AuthQuickstart", "getCurrentUser failed with an exception: $error")
        }
    }

    suspend fun alreadySignedIn(){
        try {
            val result = Amplify.Auth.getCurrentUser()
            Log.i("AuthQuickstart", "Current user details are: $result")
        } catch (error: Exception) {
            Log.e("AuthQuickstart", "getCurrentUser failed with an exception: $error")
        }
    }

    suspend fun getUserId(): String{
        var result = ""
        try {
            val userId = Amplify.Auth.getCurrentUser().userId
            Log.i("AuthQuickstart", "Current user details are: $result")
            result = userId
        } catch (error: Exception) {
            Log.e("AuthQuickstart", "getCurrentUser failed with an exception: $error")
        }
        return result
    }

    suspend fun registerUser(email:String, password: String){
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        try {
            val result = Amplify.Auth.signUp(email, password, options)
            Log.i("AuthQuickStart", "Result: $result")
        } catch (error: AuthException) {
            Log.e("AuthQuickStart", "Sign up failed", error)
        }
    }

    suspend fun confirmSignUp(email: String, code:String){
        try {
            val result = Amplify.Auth.confirmSignUp(email, code)
            if (result.isSignUpComplete) {
                Log.i("AuthQuickstart", "Signup confirmed")
            } else {
                Log.i("AuthQuickstart", "Signup confirmation not yet complete")
            }
        } catch (error: AuthException) {
            Log.e("AuthQuickstart", "Failed to confirm signup", error)
        }
    }

    suspend fun firstTimeSignIn(email: String, password: String, name: String, status: String, age:String, registerNationality:String, registerCurrentLocation: String): Boolean{
        try {
            val result = Amplify.Auth.signIn(email, password)
            if (result.isSignedIn) {
                Log.i("AuthQuickstart", "Sign in succeeded")
                val newUser = User.builder()
                    .handle("")
                    .userId("")
                    .name(name)
                    .email(email)
                    .status("")
                    .birthdate(Temporal.Date(Date()))
                    .nationality("")
                    .build()
                try {
                    Amplify.DataStore.save(newUser)
                    Log.i("MyAmplifyApp", "Saved a new user successfully")
                } catch (error: DataStoreException) {
                    Log.e("MyAmplifyApp", "Error saving user", error)
                }
            } else {
                Log.e("AuthQuickstart", "Sign in not complete")
            }
        } catch (error: AuthException) {
            Log.e("AuthQuickstart", "Sign in failed", error)
            return false
        }
        return false
    }

    suspend fun signIn(email: String, password: String): Boolean{

        try {
            val result = Amplify.Auth.signIn(email, password)
            if (result.isSignedIn) {
                Log.i("AuthQuickstart", "Sign in succeeded")
            } else {
                Log.e("AuthQuickstart", "Sign in not complete")
            }
            result.isSignedIn
        } catch (error: AuthException) {
            Log.e("AuthQuickstart", "Sign in failed", error)
            return false
        }
        return false
    }

    suspend fun signOut(){
        val signOutResult = Amplify.Auth.signOut()
        when(signOutResult) {
            is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                // Sign Out completed fully and without errors.
                Log.i("AuthQuickStart", "Signed out successfully")
            }
            is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                // Sign Out completed with some errors. User is signed out of the device.
                signOutResult.hostedUIError?.let {
                    Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                    // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.

                }
                signOutResult.globalSignOutError?.let {
                    Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.accessToken.
                }
                signOutResult.revokeTokenError?.let {
                    Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.refreshToken.
                }
            }
            is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                // Sign Out failed with an exception, leaving the user signed in.
                Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
            }
        }
    }
}
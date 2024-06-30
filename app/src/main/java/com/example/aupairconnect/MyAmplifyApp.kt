package com.example.aupairconnect

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import android.content.Context

class MyAmplifyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        MyAmplifyApp.appContext = applicationContext

        try {
//            val modelProvider: AmplifyModelProvider = AmplifyModelProvider.getInstance()
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }

    companion object {

        lateinit  var appContext: Context

    }
}
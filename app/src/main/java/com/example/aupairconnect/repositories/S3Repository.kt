package com.example.aupairconnect.repositories

import android.util.Log
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StorageException
import java.io.File

class S3Repository {

    suspend fun uploadRegisterProfile(email: String, file: File) {
        System.out.println("We are uploading photo")
//        System.out.println(file.)
        file.writeText("Example file contents")
        val upload = Amplify.Storage.uploadFile("ExampleKey", file)
        try {
            val result = upload.result()
            Log.i("MyAmplifyApp", "Successfully uploaded: ${result.key}")
        } catch (error: StorageException) {
            Log.e("MyAmplifyApp", "Upload failed", error)
        }
    }

    fun uploadUserPhoto(){

    }

    fun uploadUserVideo(){

    }
}
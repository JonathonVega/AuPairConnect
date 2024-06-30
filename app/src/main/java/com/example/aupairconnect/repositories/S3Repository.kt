package com.example.aupairconnect.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.StoragePath
import com.example.aupairconnect.MyAmplifyApp.Companion.appContext
import com.example.aupairconnect.domain.model.Aupair
import java.io.File

class S3Repository {

    companion object {

//        fun applicationContext() : Context {
//            return instance!!.applicationContext
//        }

        suspend fun getMainProfileImage(userId: String): File {

            val localFile = File("${appContext.filesDir}/downloaded-image.jpg")
            try {
                println("Getting S3 image")

                val download = Amplify.Storage.downloadFile(StoragePath.fromString("public/images/$userId/profileImage1.jpg"), localFile)
                try {
                    val fileName = download.result().file

                    Log.i("MyAmplifyApp", "Successfully downloaded: $fileName")
                    return fileName
                } catch (error: StorageException) {
                    Log.e("MyAmplifyApp", "Download Failure", error)
                }
            } catch (e: Exception) {
                Log.e("S3: Download","Main Image Download Failure", e)
            }
            //TODO: fix issue when it hits here
            return localFile
        }

        suspend fun getProfileImages(userId: String): ArrayList<File> {

//            val localFile = File("${appContext.filesDir}/downloaded-image.jpg")
            var images: ArrayList<File> = arrayListOf()
            try {
                println("Getting S3 images")
                for(i in 1..6){
                    val localFile = File("${appContext.filesDir}/$userId/downloaded-image$i.jpg")
                    try {
                        println("public/images/$userId/profileImage$i.jpg")
                        val download = Amplify.Storage.downloadFile(StoragePath.fromString("public/images/$userId/profileImage$i.jpg"), localFile)
                        val fileName = download.result().file
                        images.add(fileName)
                        Log.i("MyAmplifyApp", "Successfully downloaded: $fileName")

                    } catch (error: StorageException) {
                        println("$i")
                        Log.e("MyAmplifyApp", "Image Download Failure", error)
                    }
                }
                println("returning full? ${images.size}")
                return images

            } catch (e: Exception) {
                Log.e("S3: Download","Supporting Images Download Failure", e)
            }
            //TODO: fix issue when it hits here
            println("returning empty? ${images.size}")
            return images
        }
    }
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
}
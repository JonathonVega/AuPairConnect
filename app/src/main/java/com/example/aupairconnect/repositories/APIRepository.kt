package com.example.aupairconnect.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.util.Log
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.generated.model.User
import com.amplifyframework.kotlin.core.Amplify
import com.example.aupairconnect.MyAmplifyApp.Companion.appContext
import com.example.aupairconnect.domain.model.Aupair
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class APIRepository() {

    companion object {
        suspend fun getUserById(id: String): com.example.aupairconnect.domain.model.User{
            try {
                val response = Amplify.API.query(ModelQuery.get(User::class.java, id))
                Log.i("MyAmplifyApp", response.data.name)
                return convertModelToUser(user = response.data)
            } catch (error: ApiException) {
                Log.e("MyAmplifyApp", "Query failed", error)
            }
            return getEmptyUser()
        }

        suspend fun getUserData(): com.example.aupairconnect.domain.model.User{
            try {
                val userId = AuthRepository.getUserId()
                val response = Amplify.API.query(ModelQuery.get(User::class.java, userId))
                Log.i("MyAmplifyApp", response.data.name)
                return convertModelToUser(user = response.data)
            } catch (error: ApiException) {
                Log.e("MyAmplifyApp", "Query failed", error)
            }
            return getEmptyUser()
        }

        suspend fun getListOfNearbyAupairs(aupairUser: com.example.aupairconnect.domain.model.User): List<Aupair>{
            try {
                val minLatitude = aupairUser.latitude?.let {
                    it - 0.6
                } ?: 0.0
                val maxLatitude = aupairUser.latitude?.let {
                    it + 0.6
                } ?: 0.0
                val minLongitude = aupairUser.longitude?.let {
                    it - 0.6
                } ?: 0.0
                val maxLongitude = aupairUser.longitude?.let {
                    it + 0.6
                } ?: 0.0
                val predicate = User.USER_ID.ne(aupairUser.userId)
//                    .and(User.BLOCKED_USERS.notContains(aupairUser.userId)) //TODO: Add filter for blocked users
                    .and(User.ACTIVE.eq(true))
                    .and(User.LATITUDE.between(minLatitude, maxLatitude))
                    .and(User.LONGITUDE.between(minLongitude, maxLongitude))

                println("yojone oehj" + aupairUser.userId)
                val res = Amplify.API
                    .query(ModelQuery.list(User::class.java, predicate))//NAME.contains("first")))
                println("Got a list of names!!!!")
                println(res.data.items.first().name)
                println(res.data.items.first().userId)
                var results = arrayListOf<Aupair>()//mutableListOf<User>()
                res.data.items.forEach{ item ->
                    if(aupairUser.userId != null ){
                        println("here1")
                        if(item.blockedUsers == null){
                            print("Item added!!!!")
                            val aupairObject = mapUserToAupairClass(item)
//                            val aupairImageFile = S3Repository.getMainProfileImage(aupairObject.userId)
//
//                            val bitmap: Bitmap? = BitmapFactory.decodeFile(aupairImageFile.path)
//                            if (bitmap != null) {
//                                aupairObject.profileImage = bitmap
//                            }

                            val aupairImagesFiles = S3Repository.getProfileImages(aupairObject.userId)
                            println("Images come through?: ${aupairImagesFiles.size}")
                            var tempAupairImages: ArrayList<Bitmap> = arrayListOf()
                            if (aupairImagesFiles.size >= 1){//.isNotEmpty()){
                                for (i in aupairImagesFiles) {
                                    println(i.name)
                                    val bitmap: Bitmap? = BitmapFactory.decodeFile(i.path)
                                    if (bitmap != null) {
                                        tempAupairImages.add(bitmap)
                                    }
                                }
                                if (tempAupairImages.isNotEmpty()){

                                    aupairObject.profileImages = tempAupairImages
                                    println("Size of images is ${aupairObject.profileImages.size}")
                                }

                            }




//                            imageView.setImageBitmap(bitmap)

                            results.add(aupairObject)
                        } else {
                            if (!item.blockedUsers.contains(aupairUser.userId)){
                                print("Item added!!!!")
                                val aupairObject = mapUserToAupairClass(item)
                                results.add(aupairObject)
                            }
                        }

                    }
                }
                print("result length is " + results.size)
                return(results)
            } catch (error: ApiException) {
                Log.e("MyAmplifyApp", "Query failure", error)
            }
            return arrayListOf()
        }


        // Helper Methods

        fun mapUserToAupairClass(user: User): Aupair{
            return Aupair(
                id = user.id,
                name = user.name,
                userId = user.userId,
                handle = user.handle,
                status = user.status,
                birthdate = LocalDate.now(),//LocalDate.parse(user.birthdate.toString()., DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")),
//                birthdate = user.birthdate.
                bio = user.bio,
                email = user.email,
                nationality = user.nationality,
                city = user.city,
                state = user.state,
                country = user.country,
                zipcode = user.zipcode,
                latitude = user.latitude,
                longitude = user.longitude,
                chatroomsIds = user.chatroomsIds,
//                blockedUsers = user.blockedUsers, //TODO: add blocked users list
                //token = user.token //TODO: May need to pull to get token
                active = user.active,


            )
        }
        fun convertModelToUser(user: User): com.example.aupairconnect.domain.model.User {
            return com.example.aupairconnect.domain.model.User(
                id = user.id,
                handle = user.handle,
                userId = user.userId,
                name = user.name,
                email = user.email,
                status = user.status,
                birthdate = user.birthdate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                nationality = user.nationality,
                bio = user.bio,
                createdAt = user.createdAt.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                city = user.city,
                state = user.state,
                country = user.country,
                zipcode = user.zipcode,
                latitude = user.latitude,
                longitude = user.longitude,
                chatroomsIds = user.chatroomsIds,
                blockedUsers = user.blockedUsers,
                active = user.active)
        }

        fun getEmptyUser(): com.example.aupairconnect.domain.model.User {
            return com.example.aupairconnect.domain.model.User(
                id = "",
                handle = "",
                userId = "",
                name = "",
                email = "",
                status = "",
                birthdate = LocalDate.now(),
                nationality = "",
                bio = "",
                createdAt = LocalDate.now(),
                city = "",
                state = "",
                country = "",
                zipcode = "",
                latitude = 0.0,
                longitude = 0.0,
                chatroomsIds = emptyList(),
                blockedUsers = emptyList(),
                active = true)
        }
    }



}
package com.example.aupairconnect.repositories

import android.util.Log
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.generated.model.User
import com.amplifyframework.kotlin.core.Amplify
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class APIRepository() {
    val authRepository = AuthRepository()
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
            val userId = authRepository.getUserId()
            val response = Amplify.API.query(ModelQuery.get(User::class.java, userId))
            Log.i("MyAmplifyApp", response.data.name)
            return convertModelToUser(user = response.data)
        } catch (error: ApiException) {
            Log.e("MyAmplifyApp", "Query failed", error)
        }
        return getEmptyUser()
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
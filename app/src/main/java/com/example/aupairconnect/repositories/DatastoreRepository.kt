package com.example.aupairconnect.repositories

import android.util.Log
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.User
import com.amplifyframework.kotlin.core.Amplify
//import com.amplifyframework.core.Amplify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch

class DatastoreRepository() {

    suspend fun getUserData(email: String): com.example.aupairconnect.domain.model.User {
        var userObject = com.example.aupairconnect.domain.model.User()
        val listOfUsers = mutableListOf<User>();
        Amplify.DataStore.query(User::class, Where.matches(User.EMAIL.eq(email)))
            .catch { Log.e("MyAmplifyApp", "Query failed", it) }
            .collect {
                Log.i("MyAmplifyApp", "User email in viewmodel: ${it.email}")
                val userData = com.example.aupairconnect.domain.model.User(it.name, it.age, it.nationality, it.currentLocation, it.status, it.bio)
                listOfUsers.add(it)
                userObject = com.example.aupairconnect.domain.model.User(
                    listOfUsers[0].name,
                    listOfUsers[0].age,
                    listOfUsers[0].nationality,
                    listOfUsers[0].currentLocation,
                    listOfUsers[0].status,
                    listOfUsers[0].bio
                )
            }
        return userObject
    }
}
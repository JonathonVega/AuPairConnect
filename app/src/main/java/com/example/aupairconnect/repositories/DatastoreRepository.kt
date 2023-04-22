package com.example.aupairconnect.repositories

import android.util.Log
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.User
//import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.core.Amplify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch

class DatastoreRepository() {

//    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserData(email: String) {
//        var resultUser: com.example.aupairconnect.domain.model.User? = null
        println("Made it in useraccountdata with $email")
//        Amplify.DataStore.query(
//            User::class,
//            Where.matches(User.EMAIL.eq(email)))
//            .catch { Log.e("MyAmplifyApp", "Query failed", it) }
//            .collect {
//                if(it.)
////                resultUser = com.example.aupairconnect.domain.model.User(it.name, it.age, it.nationality, it.currentLocation, it.status, it.bio)
//                Log.i("MyAmplifyApp", "User: $it")
//            }
//        try {
//            val response = Amplify.API.query(ModelQuery.get(User::class.java, email))
//            Log.i("MyAmplifyApp", response.data.name)
//        } catch (error: ApiException) {
//            Log.e("MyAmplifyApp", "Query failed", error)
//        }
//        return resultUser
        println("Yes, we are here!!!!!!!!*********")
        var user: User? = null
        Amplify.DataStore.query(User::class.java,
//            Where.matches(User.EMAIL.eq(email)),
            { users ->
                if (users.hasNext()) {
                    val userModel = users.next()
                    println("Down to the core!!!!!&&&&******")
                    Log.i("MyAmplifyApp", "Post: $userModel")
                }
            },
            { Log.e("MyAmplifyApp", "Query failed", it) }
        )
    }
}
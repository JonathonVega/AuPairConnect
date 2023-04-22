package com.example.aupairconnect.presentation.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.DataStoreException
import com.example.aupairconnect.MainActivity
import com.example.aupairconnect.StoreUserEmail
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.repositories.DatastoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.QueryRequest
import kotlin.system.exitProcess

class ProfileViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    var user: User? = null
    var userName = mutableStateOf("")
    var nationality = mutableStateOf("")

//    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            val user = datastoreRepository.getUserAccountData()
//            user.name?.let {
//                userName.value = it
//            }
//            user.nationality?.let{
//                nationality.value = it
//            }
//        }
//    }

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

    fun getUserData(email: String) {
        println("We will get the userdata!!!!!!!!")
        Amplify.API.query(ModelQuery.get(User::class.java, email),
            { Log.i("MyAmplifyApp", "Query results = ${(it.data as User).name}") },
            { Log.e("MyAmplifyApp", "Query failed", it) }
        );

    }

//    suspend fun queryDynTable(
//        tableNameVal: String,
//        partitionKeyName: String,
//        partitionKeyVal: String,
//        partitionAlias: String
//    ): Int {
//
//        val attrNameAlias = mutableMapOf<String, String>()
//        attrNameAlias[partitionAlias] = partitionKeyName
//
//        // Set up mapping of the partition name with the value.
//        val attrValues = mutableMapOf<String, AttributeValue>()
//        attrValues[":$partitionKeyName"] = AttributeValue.S(partitionKeyVal)
//
//        val request = QueryRequest {
//            tableName = tableNameVal
//            keyConditionExpression = "$partitionAlias = :$partitionKeyName"
//            expressionAttributeNames = attrNameAlias
//            this.expressionAttributeValues = attrValues
//        }
//
//        DynamoDbClient { region = "us-east-1" }.use { ddb ->
//            val response = ddb.query(request)
//            return response.count
//        }
//    }
//        CoroutineScope(Dispatchers.IO).launch {
//            println("We will get the userdata!!!!!!!!")
//
//
//            Amplify.DataStore.query(
//                com.amplifyframework.datastore.generated.model.User::class.java,
////            Where.matches(User.EMAIL.eq(email)),
//                { users ->
//                    if (users.hasNext()) {
//                        val userModel = users.next()
//                        println("Down to the core!!!!!&&&&******")
//                        Log.i("MyAmplifyApp", "Post: $userModel")
//                    }
//                },
//                { Log.e("MyAmplifyApp", "Query failed", it) }
//            )
////            datastoreRepository.getUserData(email)
////            user = datastoreRepository.getUserAccountData(email)
////            user?.name.let {
////                if (it != null) {
////                    userName.value = it
////                }
////            }
////            println(userName.value)
//            println("Did we start getting the userdata!!!!!!!!")
//        }

}
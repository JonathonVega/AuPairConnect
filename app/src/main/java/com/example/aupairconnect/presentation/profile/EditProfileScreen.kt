package com.example.aupairconnect.presentation.profile

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.presentation.ui.theme.ACTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    onNavigation: NavHostController,
    viewModel: ProfileViewModel,
    userData: User,
    userEmail:String
){

    val editAge = remember { mutableStateOf("") }
    val editCurrentLocation = remember { mutableStateOf("") }
    Column() {
        TextField(value = viewModel.profileName.value,
            onValueChange = {viewModel.profileName.value = it},
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = "Enter Name") }
        )
        AupairSegmentedButtons(viewModel)
        OriginCountryDropdown(viewModel)
        TextField(value = editAge.value,
            onValueChange = { editAge.value = it},
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "What is your age?")})
        Text(text = "we will print in edit profile the name: ${userData.name}")
        Button(onClick = {

            saveAndQuery(onNavigation, userEmail)


//            com.amplifyframework.kotlin.core.Amplify.DataStore.query(
//                com.amplifyframework.datastore.generated.model.User::class, Where.matches(
//                    com.amplifyframework.datastore.generated.model.User.EMAIL.eq(email)))
//                .catch { Log.e("MyAmplifyApp", "Query failed", it) }
//                .collect {
//                    Log.i("MyAmplifyApp", "User email in viewmodel: ${it.email}")
//                    val userData = com.example.aupairconnect.domain.model.User(it.name, it.age, it.nationality, it.currentLocation, it.status, it.bio)
//                    listOfUsers.add(it)
//                    userObject = com.example.aupairconnect.domain.model.User(
//                        listOfUsers[0].name,
//                        listOfUsers[0].age,
//                        listOfUsers[0].nationality,
//                        listOfUsers[0].currentLocation,
//                        listOfUsers[0].status,
//                        listOfUsers[0].bio
//                    )
//                }



        }) {
            Text(text = "Save")
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OriginCountryDropdown(viewModel: ProfileViewModel){
    var expanded = remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        TextField(
            readOnly = true,
            value = viewModel.editNationality.value,
            onValueChange = { },
            label = { Text("Select Your Nationality") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            viewModel.COUNTRY_LIST.forEach { country ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.editNationality.value = country
                        expanded.value = false
                    }
                ) {
                    Text(text = country)
                }
            }
        }
    }
}

@Composable
fun AupairSegmentedButtons(viewModel: ProfileViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val cornerRadius = 16.dp
        var selectedIndex = remember { mutableStateOf(-1) }

        val itemsList = listOf<String>("Aupair", "Ex-Aupair")

        itemsList.forEachIndexed { index, item ->

            OutlinedButton(
                onClick = { viewModel.editStatus.value = item
                    selectedIndex.value = index},
                modifier = when (index) {
                    0 ->
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(if (selectedIndex.value == index) 1f else 0f)
                            .width(120.dp)
                            .height(40.dp)
                    else ->
                        Modifier
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedIndex.value == index) 1f else 0f)
                            .width(120.dp)
                            .height(40.dp)
                },
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp
                    )
                    itemsList.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius
                    )
                    else -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                },
                border = BorderStroke(
                    1.dp, if (selectedIndex.value == index) {
                        ACTheme
                    } else {
                        ACTheme.copy(alpha = 0.75f)
                    }
                ),
                colors = if (selectedIndex.value == index) {
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = ACTheme.copy(alpha = 0.5f),
                        contentColor = ACTheme
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.White,
                        contentColor = ACTheme
                    )
                }
            ) {
                Text(item)
            }
        }
    }
}

fun saveAndQuery(onNavigation: NavHostController, email: String){
    Amplify.DataStore.clear(
        { Log.i("MyAmplifyApp", "DataStore cleared") },
        { Log.e("MyAmplifyApp", "Error clearing DataStore", it) }
    )
    println("yea")
    Amplify.DataStore.query(com.amplifyframework.datastore.generated.model.User::class.java,
        Where.matches(com.amplifyframework.datastore.generated.model.User.EMAIL.eq(email)),
        { users ->
            println("yea2")
            while (users.hasNext()) {
                val post = users.next()
                Log.i("MyAmplifyApp", "User from edit: $users")
                onNavigation.popBackStack()
            }
        },
        { Log.e("MyAmplifyApp", "Query failed", it) }
    )
    println("yea3")

}
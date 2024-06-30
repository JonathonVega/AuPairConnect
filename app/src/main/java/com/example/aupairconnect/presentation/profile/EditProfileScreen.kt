package com.example.aupairconnect.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.presentation.ui.theme.ACTheme
import com.example.aupairconnect.repositories.APIRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(
    onNavigation: NavHostController,
    viewModel: ProfileViewModel,
    userData: User,
    userEmail:String
){

    val editAge = remember { mutableStateOf("") }
    val editCurrentLocation = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(modifier = Modifier
        .padding(start = 25.dp, end = 25.dp)
        .clickable { keyboardController?.hide() }
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(value = viewModel.profileName.value,
            onValueChange = {viewModel.profileName.value = it},
            placeholder = { Text("${viewModel.profileName.value}") },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = "Enter Name") }
        )
        AupairSegmentedButtons(viewModel)
        OriginCountryDropdown(viewModel)
        TextField(value = viewModel.profileAge.value,
            onValueChange = { viewModel.profileAge.value = it},
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "What is your age?")})
        Text(text = "we will print in edit profile the name: ${viewModel.profileName.value}")
        Button(onClick = {

            saveAndQuery(onNavigation,viewModel ,userEmail)


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
            value = viewModel.profileNationality.value,
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
                        viewModel.profileNationality.value = country
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
                onClick = { viewModel.profileStatus.value = item
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

fun saveAndQuery(onNavigation: NavHostController, viewModel: ProfileViewModel, email: String){
    var userInfo: User? = null
    val apiRepository = APIRepository()
//    CoroutineScope(Dispatchers.IO).launch {
//
//
//    }
    runBlocking {
//        datastoreRepository.clearDataStore()
        println("Email is $email")
        userInfo = APIRepository.getUserData()
//        val dummyInfo = User("john", 5, "South America", "Here", "Aupair", "Yay, I'm here!")
        viewModel.insertUserDataToViewModel(userInfo!!)
        println("Lets check this out!!!!!!!!!!!!!!")
        println(userInfo)
        println(userInfo!!.name)


        println("yea")
        delay(2500)
    }

//    if(userInfo != null || userInfo?.name != null){
//        onNavigation.popBackStack()
//    }
    onNavigation.popBackStack()

//    val route = Screens.ProfileScreen.route
//    onNavigation.navigate(route){
//        popUpTo(route)
//    }
//    onNavigation.popBackStack(Screens.EditProfileScreen.route, true, true)

//    Amplify.DataStore.query(com.amplifyframework.datastore.generated.model.User::class.java,
//        Where.matches(com.amplifyframework.datastore.generated.model.User.EMAIL.eq(email)),
//        { users ->
//            println("yea2")
//            while (users.hasNext()) {
//                val user = users.next()
//                Log.i("MyAmplifyApp", "User from edit: $user")
//            }
//        },
//        { Log.e("MyAmplifyApp", "Query failed", it) }
//    )

}
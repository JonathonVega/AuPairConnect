package com.example.aupairconnect

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.presentation.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigation: NavHostController,
    viewModel: ProfileViewModel
){

    //TODO: logoutState seems useless. Read later and delete
    val logoutState = remember{ mutableStateOf(false) }
    val context = LocalContext.current
//    val datastore = StoreUserEmail(context)
//    val savedEmail = datastore.getEmail.collectAsState(initial = "")
//    viewModel.userEmail = savedEmail.value
    var x:String?

    Column(modifier = Modifier
        .padding(start = 75.dp, end = 75.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileImage()
//        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
        Text(text = "This is the Profile Screen", fontSize = 20.sp)
        println(viewModel.userName.value)
//        if(viewModel.userName.value.isNotEmpty()){
//            x = viewModel.userName.value
//            println("on profile UI, we are getting $x")
//            Text(text = "$x", fontSize = 20.sp)
//        }
        viewModel.userName.value.let { Text(text = it, fontSize = 20.sp) }
        viewModel.userName.value.let { Text(text = "Age: $it", fontSize = 20.sp) }
        viewModel.nationality.value.let { Text(text = "Nationality: $it", fontSize = 20.sp) }
        Text(text = "Living In: Alexandria, Virginia, United States", fontSize = 20.sp)
        Button(onClick = {

            //TODO:Uncomment later once Home UI is built out.
//            viewModel.signOut()
            logoutState.value = true
        }) {
            Text(text = "Log out")
            if(logoutState.value){
                viewModel.signOut(LocalContext.current)
            }
        }
        //TODO: Delete the commented code if not needed anymore
//        Button(onClick = { viewModel.getCurrentUser() }) {
//            Text(text = "Check current user")
//        }
    }
}

@Composable
fun ProfileImage(){
    androidx.compose.foundation.Image(
        painter = painterResource(id = R.drawable.baseline_person_24),
        contentDescription = "Image",
        modifier = Modifier
            .border(border = BorderStroke(1.dp, color = Color.Black), shape = CircleShape)
            .width(100.dp)
            .height(100.dp)
            .clip(shape = CircleShape))
}

@Composable
fun LogOut(){
    val activity = (LocalContext.current as? Activity)
    activity?.finish()
    activity?.startActivity(Intent(activity, MainActivity::class.java))
}
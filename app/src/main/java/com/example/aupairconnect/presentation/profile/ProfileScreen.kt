package com.example.aupairconnect

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.presentation.discover.DiscoverViewModel
import com.example.aupairconnect.presentation.profile.EditProfileScreen
import com.example.aupairconnect.presentation.profile.ProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    onNavigation: NavHostController,
    viewModel: ProfileViewModel
){

    //TODO: logoutState seems useless. Read later and delete
    val logoutState = remember{ mutableStateOf(false) }
    val context = LocalContext.current
    val photoList = mutableListOf("Photo1","Photo2","Photo3","Photo4")
//    viewModel.editName.value = userData.name

    Column(modifier = Modifier
        .padding(start = 25.dp, end = 25.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileImage()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${viewModel.userProfileData.name}", fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text(text = "${viewModel.profileStatus.value}",color = Color.Gray ,fontSize = 20.sp)
            Text(text = "From: ${viewModel.profileNationality.value}", fontSize = 20.sp)
        }
        Divider(thickness = Dp.Hairline, color = Color.Black)
        Text(text = "Age: ${viewModel.profileAge.value}", fontSize = 20.sp)
        Text(text = "Living In: Alexandria, Virginia, United States", fontSize = 20.sp, textAlign = TextAlign.Center)
        Divider(thickness = Dp.Hairline, color = Color.Black)
        LazyRow(modifier = Modifier.fillMaxWidth()){
            itemsIndexed(photoList){ index, photo ->
                RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto3))
                RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto1))
                RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto5))
                RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto2))
            }
        }
        Divider(thickness = Dp.Hairline, color = Color.Black)
        println("bio is ${viewModel.profileBio.value}")
        if (viewModel.profileBio.value.isBlank() || viewModel.profileBio.value == "null"){
//            Text(text = "Bio is under construction \uD83D\uDEA7", fontSize = 20.sp)
            Text(text = "Hi, I'm from this country. Looking for friends that want to party and hang out. I am a engineer by heart and will occasionally work on projects at home when I have nothing to do. Favorite food is japanese, so if you got a recommendation, let me know!", fontSize = 20.sp)
        } else {
            Text(text = "${viewModel.profileBio.value}", fontSize = 20.sp)

        }
        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {

                //TODO:Uncomment later once Home UI is built out.
//            viewModel.signOut()
                logoutState.value = true
            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
                Text(text = "Log out")
                if(logoutState.value){
                    viewModel.signOut(LocalContext.current)
                }
            }
            Button(onClick = {
//                val route = Screens.EditProfileScreen.route
//                onNavigation.navigate(route)
//                RegisterScreen(onNavigation = navController, viewModel = authViewModel)
                val route = Screens.EditProfileScreen.route
                onNavigation.navigate(route)
            }) {
                Text("Edit Profile")
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

@Composable
fun RoundedImage(painter: Painter){
    val shape =  RoundedCornerShape(24.dp)
    Image(painter = painter,
        contentDescription = "Aupair Photo",
        contentScale = ContentScale.Fit,
        modifier = Modifier
//            .height(100.dp)
            .size(100.dp)
            .clip(shape)
            .background(Color.White, shape)
            .border(0.dp, Color.White, shape))
}

fun insertUserDataToViewModel(userData: User, viewModel: ProfileViewModel){
    viewModel.profileName.value = userData.name.toString()
    viewModel.profileStatus.value = userData.status.toString()
    viewModel.profileAge.value = userData.birthdate.toString()
    viewModel.profileNationality.value = userData.nationality.toString()
    viewModel.profileCurrentLocation.value = userData.latitude.toString()
    viewModel.profileBio.value = userData.bio.toString()
}
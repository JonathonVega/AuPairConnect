package com.example.aupairconnect

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aupairconnect.presentation.ui.theme.ACTheme
import com.example.aupairconnect.presentation.auth.AuthViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    onNavigation: NavHostController,
    viewModel: AuthViewModel
){

    var selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri.value = uri }
    )

    var passwordVisibility = remember { mutableStateOf(false) }
    var confirmPasswordVisibility = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    //TODO: Add loading spinner when getting current location

    HorizontalPager(count = 4,
        state = pagerState,
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxSize()
            .clickable { keyboardController?.hide() }) { page ->
        when(page){
            0 -> {
                Column(
                    modifier = Modifier.width(280.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Sign Up", fontSize = 40.sp)

                    TextField(value = viewModel.registerEmail.value,
                        onValueChange = {viewModel.registerEmail.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text(text = "Enter Email")})
                    TextField(value = viewModel.registerPassword.value,
                        onValueChange = {viewModel.registerPassword.value = it},
                        label = {Text(text = "Enter Password")},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if(passwordVisibility.value)
                                ImageVector.vectorResource(id = R.drawable.baseline_visibility_24)
                            else
                                ImageVector.vectorResource(id = R.drawable.baseline_visibility_off_24)
                            val description = if (passwordVisibility.value) "Hide password" else "Show password"

                            IconButton(onClick = {passwordVisibility.value = !passwordVisibility.value}){
                                Icon(imageVector  = image, description)
                            }
                        }
                    )
                    TextField(value = viewModel.registerConfirmPassword.value,
                        onValueChange = {viewModel.registerConfirmPassword.value = it},
                        label = {Text(text = "Confirm Password")},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if(confirmPasswordVisibility.value)
                                ImageVector.vectorResource(id = R.drawable.baseline_visibility_24)
                            else
                                ImageVector.vectorResource(id = R.drawable.baseline_visibility_off_24)
                            val description = if (confirmPasswordVisibility.value) "Hide password" else "Show password"

                            IconButton(onClick = {confirmPasswordVisibility.value = !confirmPasswordVisibility.value}){
                                Icon(imageVector  = image, description)
                            }
                        }
                    )

                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }){
                        Text(text = "Continue")
                    }


                    //TODO Programmatically go back instead of loading new screen
                    Row(modifier = Modifier.padding(top = 50.dp)){
                        Text(text = "Already have an account? ")
                        ClickableText(text = AnnotatedString("Sign in"), onClick = {viewModel.goToSignInScreen()}, style = TextStyle(fontSize = 16.sp, color = ACTheme))
                    }
                }
            }
            1 -> {
                Column(modifier = Modifier.padding(start = 75.dp, end = 75.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    if(selectedImageUri.value != null){
                        Box(contentAlignment = Alignment.BottomEnd) {
                            AsyncImage(
                                model = selectedImageUri.value,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .border(
                                        border = BorderStroke(1.dp, color = Color.Black),
                                        shape = CircleShape
                                    )
                                    .width(150.dp)
                                    .height(150.dp)
                                    .clip(shape = CircleShape)
                                    .clickable {
                                        singlePhotoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                            )
                            Box(modifier = Modifier.padding(1.dp).border(2.dp, Color.Black, shape = RectangleShape), contentAlignment = Alignment.CenterEnd) {
                                Icon(painter = painterResource(id = R.drawable.baseline_edit_24),
                                    contentDescription = "edit icon",
                                    modifier = Modifier.size(30.dp))
                                
                            }
                        }

                    } else {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.baseline_person_24),
                                contentDescription = "Image",
                                modifier = Modifier
                                    .border(
                                        border = BorderStroke(1.dp, color = Color.Black),
                                        shape = CircleShape
                                    )
                                    .width(150.dp)
                                    .height(150.dp)
                                    .clip(shape = CircleShape)
                                    .clickable {
                                        singlePhotoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                            )
                            Box(modifier = Modifier.padding(1.dp).border(2.dp, Color.Black, shape = RectangleShape), contentAlignment = Alignment.CenterEnd) {
                                Icon(painter = painterResource(id = R.drawable.baseline_edit_24),
                                    contentDescription = "edit icon",
                                    modifier = Modifier.size(30.dp))
                            }
                        }
                    }


                    TextField(value = viewModel.registerName.value,
                        onValueChange = {viewModel.registerName.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text(text = "Enter Your Name")})
                    TextField(value = viewModel.registerAge.value,
                        onValueChange = { viewModel.registerAge.value = it},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {Text(text = "What is your age?")})
                    OriginCountryDropdown(viewModel)
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 0.dp)) {
                        TextField(modifier = Modifier
                            .weight(3f)
                            .padding(end = 15.dp),
                            value = viewModel.registerCurrentLocation.value,
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                            onValueChange = {viewModel.registerCurrentLocation.value = it},
                            enabled = false,
                            label = {Text(text = "Current Location?")},
                            trailingIcon = {
                                val image = Icons.Filled.Place
                                IconButton(modifier = Modifier.size(50.dp),
                                    onClick = {viewModel.getCurrentLocation(context)}){
                                    androidx.compose.material.Icon(imageVector = image, "Current Location", tint = ACTheme)
                                }
                            })
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Button(onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }){
                            Text(text = "Back")
                        }
                        Button(onClick = {
                            if(viewModel.registerPassword.value == viewModel.registerConfirmPassword.value){
                                println("password confirmed")
                                //TODO: Put call to AWS into viewModel
                                viewModel.registerUser()

                                val route = Screens.VerifyScreen.route
                                onNavigation.navigate(route)
                            }
                        }){
                            Text(text = "Sign Up")
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun profileRegisterImage(){
////    var image: Int = R.drawable.baseline_person_24
//    var selectedImageUri = remember {mutableStateOf<Uri?>(null)}
//
//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { uri -> selectedImageUri.value = uri }
//    )
//    if(uri != null){
//        AsyncImage(
//            model = uri,
//            contentDescription = null,
//            modifier = Modifier
//                .border(border = BorderStroke(1.dp, color = Color.Black), shape = CircleShape)
//                .width(100.dp)
//                .height(100.dp)
//                .clip(shape = CircleShape)
//                .clickable { singlePhotoPickerLauncher.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )},
//            contentScale = ContentScale.Crop
//        )
//    } else {
//        androidx.compose.foundation.Image(
//            painter = painterResource(id = R.drawable.baseline_person_24),
//            contentDescription = "Image",
//            modifier = Modifier
//                .border(border = BorderStroke(1.dp, color = Color.Black), shape = CircleShape)
//                .width(100.dp)
//                .height(100.dp)
//                .clip(shape = CircleShape)
//                .clickable { }
//        )
//    }
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OriginCountryDropdown(viewModel: AuthViewModel){
    var expanded = remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        TextField(
            readOnly = true,
            value = viewModel.registerCountryOrigin.value,
            onValueChange = { },
            label = { Text("Select Your Country") },
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
                        viewModel.registerCountryOrigin.value = country
                        expanded.value = false
                    }
                ) {
                    Text(text = country)
                }
            }
        }
    }
}

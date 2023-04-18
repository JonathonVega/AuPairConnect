package com.example.aupairconnect

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aupairconnect.presentation.auth.AuthViewModel
import com.example.aupairconnect.presentation.ui.theme.ACTheme
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

    val localContext = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.selectedImageUri.value = uri }
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
                            viewModel.continueSignUp(pagerState)
                        }

                    }){
                        Text(text = "Continue")
                    }


                    //TODO Programmatically go back instead of loading new screen
                    Row(modifier = Modifier.padding(top = 50.dp)){
                        Text(text = "Already have an account? ")
                        ClickableText(text = AnnotatedString("Sign in"), onClick = {viewModel.goToSignInScreen()}, style = TextStyle(fontSize = 16.sp, color = ACTheme))
                    }
                    if(viewModel.errorEmailPasswordEmpty.value){
                        Text(text = "* Missing Email/Password", color = Color.Red, textAlign = TextAlign.Center)
                    }
                    if(viewModel.errorPasswordsNoMatch.value){
                        Text(text = "* Passwords do not match", color = Color.Red, textAlign = TextAlign.Center)
                    }
                }
            }
            1 -> {
                Column(modifier = Modifier.padding(start = 75.dp, end = 75.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

//                    if(viewModel.selectedImageUri.value != null){
//                        Box(contentAlignment = Alignment.BottomEnd) {
//                            AsyncImage(
//                                model = viewModel.selectedImageUri.value,
//                                contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .border(
//                                        border = BorderStroke(1.dp, color = Color.Black),
//                                        shape = CircleShape
//                                    )
//                                    .width(150.dp)
//                                    .height(150.dp)
//                                    .clip(shape = CircleShape)
//                                    .clickable {
//                                        singlePhotoPickerLauncher.launch(
//                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                                        )
//                                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                                    }
//                            )
//                            Box(modifier = Modifier
//                                .padding(1.dp)
//                                .border(2.dp, Color.Black, shape = RectangleShape), contentAlignment = Alignment.CenterEnd) {
//                                Icon(painter = painterResource(id = R.drawable.baseline_edit_24),
//                                    contentDescription = "edit icon",
//                                    modifier = Modifier.size(30.dp))
//
//                            }
//                        }
//
//                    } else {
//                        Box(contentAlignment = Alignment.BottomEnd) {
//                            androidx.compose.foundation.Image(
//                                painter = painterResource(id = R.drawable.baseline_person_24),
//                                contentDescription = "Image",
//                                modifier = Modifier
//                                    .border(
//                                        border = BorderStroke(1.dp, color = Color.Black),
//                                        shape = CircleShape
//                                    )
//                                    .width(150.dp)
//                                    .height(150.dp)
//                                    .clip(shape = CircleShape)
//                                    .clickable {
//                                        viewModel.checkStoragePermissions(context)
//                                        singlePhotoPickerLauncher.launch(
//                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                                        )
//                                    }
//                            )
//                            Box(modifier = Modifier
//                                .padding(1.dp)
//                                .border(2.dp, Color.Black, shape = RectangleShape), contentAlignment = Alignment.CenterEnd) {
//                                Icon(painter = painterResource(id = R.drawable.baseline_edit_24),
//                                    contentDescription = "edit icon",
//                                    modifier = Modifier.size(30.dp))
//                            }
//                        }
//                    }

                    AupairSegmentedButtons(viewModel)
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
//                        TextField(modifier = Modifier
//                            .weight(3f)
//                            .padding(end = 15.dp),
//                            value = viewModel.registerCurrentLocation.value,
//                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
//                            onValueChange = {viewModel.registerCurrentLocation.value = it},
//                            enabled = false,
//                            label = {Text(text = "Current Location?")},
//                            trailingIcon = {
//                                val image = Icons.Filled.Place
//                                IconButton(modifier = Modifier.size(50.dp),
//                                    onClick = {viewModel.getCurrentLocation(context)}){
//                                    androidx.compose.material.Icon(imageVector = image, "Current Location", tint = ACTheme)
//                                }
//                            })
                        TextField(value = viewModel.registerCurrentLocation.value,
                            onValueChange = {viewModel.registerCurrentLocation.value = it},
                            label = {Text(text = "Current Location?")},
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            enabled = false,
                            trailingIcon = {
                                val image = Icons.Filled.Place
                                IconButton(
                                    onClick = {viewModel.getCurrentLocation(context)}){
                                    androidx.compose.material.Icon(imageVector = image, "Current Location", tint = ACTheme)
                                }
                            }
                        )
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
                                //TODO: Put call to AWS into viewModel

//                                Glide.with(context)
//                                    .load(File(viewModel.selectedImageUri.value!!.path))
////                                    .transform(CircleTransform(..))
//                                .into(viewModel.profileImage);
                                viewModel.registerUser(context)
                            }
                        }){
                            Text(text = "Sign Up")
                        }
                    }
                    if(viewModel.errorRegisterInfoEmpty.value){
                        Text(text = "* All Info Required for Sign Up", color = Color.Red, textAlign = TextAlign.Center)
                    }
                    if(viewModel.errorAgeCharInvalid.value){
                        Text(text = "* Age had invalid characters. Please input numbers only.", color = Color.Red, textAlign = TextAlign.Center)
                    }
                    if(viewModel.errorOverAge.value){
                        Text(text = "* Age should be between 1-99", color = Color.Red, textAlign = TextAlign.Center)
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
            value = viewModel.registerNationality.value,
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
                        viewModel.registerNationality.value = country
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
fun AupairSegmentedButtons(viewModel: AuthViewModel){
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
                onClick = { viewModel.registerStatus.value = item
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
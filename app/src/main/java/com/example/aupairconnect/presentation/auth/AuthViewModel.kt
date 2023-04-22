package com.example.aupairconnect.presentation.auth

//import com.amplifyframework.core.Amplify

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.exceptions.service.UserNotConfirmedException
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.kotlin.core.Amplify
import com.example.aupairconnect.MainActivity
import com.example.aupairconnect.Screens
import com.example.aupairconnect.StoreUserEmail
import com.example.aupairconnect.graphs.Graph
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.repositories.S3Repository
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import kotlinx.coroutines.*
import java.io.*
import java.util.*


class AuthViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository,
    private val s3Repository: S3Repository
) : ViewModel() {

    // TODO: Put variables in viewModel
    //SIGN IN VARIABLES


    // REGISTER VARIABLES
    var registerEmail = mutableStateOf("")
    val registerPassword = mutableStateOf("")
    val registerConfirmPassword = mutableStateOf("")

    var selectedImageUri = mutableStateOf<Uri?>(null)
    val registerStatus = mutableStateOf("")
    val registerName = mutableStateOf("")
    val registerAge = mutableStateOf("")
    val registerNationality = mutableStateOf("")
    val registerCurrentLocation = mutableStateOf("")

    var signInSuccessful = mutableStateOf(false)
    var userNotConfirmedFailure = mutableStateOf(false)
    var alreadySignedIn = mutableStateOf(false)
    var userEmail: String = ""
    var verifyEmail: String = ""
//    lateinit var profileImage :ImageView

    // LOCATION SERVICE VARIABLES
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var permissionsManager: PermissionsManager
    var permissionsListener = getPermissionsListener()
    var userCoordinates = LatLng(0.0,0.0)

    val COUNTRY_LIST = arrayOf("Afghanistan","Albania","Algeria","Andorra","Angola","Anguilla","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas",
        "Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","British Virgin Islands","Brunei",
        "Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Colombia","Comoros","Congo","Cook Islands"
        ,"Costa Rica","Cote D Ivoire","Croatia","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea",
        "Estonia","Ethiopia","Falkland Islands","Faroe Islands","Fiji","Finland","France","French Polynesia","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada",
        "Guam","Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy","Jamaica","Japan",
        "Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Madagascar",
        "Malawi","Malaysia","Maldives","Mali","Malta","Mauritania","Mauritius","Mexico","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nepal","Netherlands",
        "Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland",
        "Portugal","Puerto Rico","Qatar","Romania","Russia","Rwanda","Saint Kitts and Nevis","Saint Lucia","Saint Vincent","Samoa","San Marino","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone",
        "Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland",
        "Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor-Leste","Togo","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Uganda","Ukraine","United Arab Emirates",
        "United Kingdom","United States","Uruguay","Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)","Yemen","Zambia","Zimbabwe")

    // ERROR MESSAGES VARIABLES
    var errorEmailPasswordEmpty = mutableStateOf(false)
    var errorPasswordsNoMatch = mutableStateOf(false)
    var errorRegisterInfoEmpty = mutableStateOf(false)
    var errorAgeCharInvalid = mutableStateOf(false)
    var errorOverAge = mutableStateOf(false)


    private var tempFile: File? = null;
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)

    fun checkIfAlreadySignedIn(email: String){
        if(email.isNotEmpty()){
            println("The already signed in email is $email")
            onNavigation.popBackStack()
            onNavigation.navigate(Graph.HOME)
        }
    }

    fun getCurrentUser(){
        CoroutineScope(Dispatchers.IO).launch {
            authRepository.getCurrentUser()
            authRepository.signOut()
        }
    }

    fun signIn(email:String, password:String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = Amplify.Auth.signIn(email, password)
                if (result.isSignedIn) {
                        Log.i("AuthQuickstart", "Sign in succeeded")
                        signInSuccessful.value = true
                    } else {
                        Log.i("AuthQuickstart", "Sign in not complete")
                        signInSuccessful.value = false
                    }
            } catch (error: AuthException){
                when(error){
                    is UserNotConfirmedException -> {
                        Log.i("UserNotConfirmedException", error.toString())
                        userNotConfirmedFailure.value = true
                    }
                    is AuthException -> {
                        Log.e("AuthQuickstart", error.toString())
                    }
                }
            }
            delay(2000)
            withContext(Dispatchers.Main){
                val datastore = StoreUserEmail(context)

                if(signInSuccessful.value){
                    datastore.saveEmail(email)
                    onNavigation.popBackStack()
                    onNavigation.navigate(Graph.HOME)

                }
                if(userNotConfirmedFailure.value){
                    userEmail = email
                    datastore.saveEmail(email)

                    verifyEmail = email
                    val route = Screens.VerifyScreen.route
                    onNavigation.navigate(route)
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    suspend fun continueSignUp(pagerState: PagerState){
        errorEmailPasswordEmpty.value = false
        errorPasswordsNoMatch.value = false
        if(registerEmail.value.isNotEmpty()
            || registerPassword.value.isNotEmpty()
            || registerConfirmPassword.value.isNotEmpty()) {
            if (registerPassword.value == registerConfirmPassword.value) {
                pagerState.animateScrollToPage(1)
                errorEmailPasswordEmpty.value = false
                errorPasswordsNoMatch.value = false
            } else {
                errorPasswordsNoMatch.value = true
            }
        } else {
            errorEmailPasswordEmpty.value = true
        }
    }

    fun registerUser(context: Context){
        errorRegisterInfoEmpty.value = false
        errorAgeCharInvalid.value = false
        errorOverAge.value = false
        if (registerStatus.value.isNotEmpty()
            && registerName.value.isNotEmpty()
            && registerAge.value.isNotEmpty()
            && registerNationality.value.isNotEmpty()
            && registerCurrentLocation.value.isNotEmpty()
            && registerCurrentLocation.value != ""){
            if (!registerAge.value.contains(",")
                || !registerAge.value.contains(".")
                || !registerAge.value.contains("-")
                || !registerAge.value.contains(" ")){
                if (registerAge.value.toInt() in 1..99){

                    errorRegisterInfoEmpty.value = false
                    errorAgeCharInvalid.value = false
                    errorOverAge.value = false
                    CoroutineScope(Dispatchers.IO).launch {
                        //TODO: Handle when error occurs in aws call. Not just string validation
                        authRepository.registerUser(registerEmail.value, registerPassword.value)
//                        uploadProfilePicture(context)
                        val datastore = StoreUserEmail(context)
                        datastore.saveEmail(registerEmail.value)
                    }

                    verifyEmail = registerEmail.value
                    val route = Screens.VerifyScreen.route
                    onNavigation.navigate(route)

                } else {
                    errorOverAge.value = true
                }
            } else {
                errorAgeCharInvalid.value = true
            }
        } else {
            errorRegisterInfoEmpty.value = true
        }
    }

    fun confirmSignUp(code:String){
        CoroutineScope(Dispatchers.IO).launch {
            authRepository.confirmSignUp(verifyEmail, code)
            authRepository.firstTimeSignIn(verifyEmail, registerPassword.value, registerName.value, registerStatus.value, registerAge.value, registerNationality.value, registerCurrentLocation.value)
        }
        onNavigation.popBackStack()
        onNavigation.navigate(Graph.HOME)
    }

    suspend fun resendVerificationCode(){
        CoroutineScope(Dispatchers.IO).launch{
        }
        try {
            val result = Amplify.Auth.resendSignUpCode(userEmail)
            Log.i("AuthDemo", "Code was sent again: $result.")
        } catch (error: AuthException){
            Log.e("AuthDemo", "Failed to resend code.", error)
        }
    }

    fun goToSignUpScreen(){
        val route = Screens.RegisterScreen.route
        onNavigation.navigate(route)
    }

    fun goToSignInScreen(){
        val route = Screens.LoginScreen.route
        onNavigation.navigate(route)
    }

//    @RequiresApi(Build.VERSION_CODES.Q)
//    suspend fun uploadProfilePicture(context: Context){
//
//        var image: Bitmap? = null
////                image = MediaStore.Images.Media.getBitmap()//getBitmap(contentResolver, imageUri)
//        val contentResolver: ContentResolver = context.contentResolver
//        val source = ImageDecoder.createSource(contentResolver, selectedImageUri.value!!)
//        val bitmap = ImageDecoder.decodeBitmap(source)
//
//        var filex: File? = null
//        filex = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + "images.jpeg")
////        withContext(Dispatchers.IO) {
////            filex.createNewFile()
////        }
//        println("YESSSSS&&^^&***&&&&&&&&&&********************")
//        println(filex.absoluteFile.toString())
//
//        try {
//            filex.createNewFile()
//        } catch (e:IOException) {
//            e.printStackTrace()
//        }
//
//
//        println("*****We got passed file creation")
//
//
//        //Convert bitmap to byte array
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
//        val bitmapdata = bos.toByteArray()
//
//        //write the bytes in file
//        val fos = FileOutputStream(filex)
//        fos.write(bitmapdata)
//        fos.flush()
//        fos.close()
//        println("!!!!!Sent it to S3!!!!!!********")
//        s3Repository.uploadRegisterProfile("examplefilex", filex)
//
//    }

    // region LOCATION PERMISSIONS

    private fun handlePermissions(context: Context, activity: MainActivity) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            Log.d("$$$$", "Location permissions granted")
        } else {
            permissionsManager = PermissionsManager(permissionsListener)
            permissionsManager.requestLocationPermissions(activity)
        }
    }

    private fun getPermissionsListener() = object : PermissionsListener {
        override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
            Log.d("$$$$", "permissionsToExplain: $permissionsToExplain")
        }

        override fun onPermissionResult(granted: Boolean) {
            if (granted) {
                Log.d("$$$$", "onPermissionResult: $granted")
            } else {
                Log.d("$$$$", "onPermissionResult: $granted")
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (checkPermissions(context)){
            if(isLocationEnabled(context)){
                var cityName: String = "City, Country"
                fusedLocationProviderClient.getCurrentLocation(100, null).addOnCompleteListener { task ->
                    val location: Location? = task.result

                    if(location != null){
                        userCoordinates = LatLng(location.latitude, location.longitude)

                        val geoCoder = Geocoder(context, Locale.getDefault())
                        val address = geoCoder.getFromLocation(location.latitude, location.longitude,3)
                        if(address != null){
                            if(address[0] != null) {
                                userCoordinates = LatLng(address[0].latitude, address[0].longitude)
                                println("${address[0].locality}, ${address[0].countryName}")
                                println(address.toString())
                                registerCurrentLocation.value = "${address[0].locality}, ${address[0].adminArea}, ${address[0].countryName}"
                            }
                        }
                    }
                }
                CoroutineScope(Dispatchers.IO).launch{
                    delay(5000)
                }
            } else {
                //setting open here
            }
        } else{
            requestPermission(context)
        }
    }

    private fun isLocationEnabled(context: Context):Boolean{
        val locationManager:LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(context: Context): Boolean{
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return
        }
        return false
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }

    private fun requestPermission(context: Context){
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    //STORAGE PERMISSIONS
    fun checkStoragePermissions(context: Context){
        println("Checking Permissions**********")
        val permission = ActivityCompat.checkSelfPermission(
            context as Activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                context as Activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    //HELPER METHODS

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
                output.close()
            }
        }
    }

//    fun createTmpFileFromUri(context: Context, uri: Uri, fileName: String): File? {
//        return try {
//            val stream = context.contentResolver.openInputStream(uri)
//            val file = File.createTempFile(fileName, "", context.cacheDir)
//            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream,file)
//            file
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }

}
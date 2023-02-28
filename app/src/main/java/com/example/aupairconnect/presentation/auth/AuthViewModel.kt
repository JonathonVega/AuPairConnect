package com.example.aupairconnect.presentation.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.exceptions.service.UserNotConfirmedException
import com.amplifyframework.core.Amplify
import com.example.aupairconnect.repositories.AuthRepository
import com.example.aupairconnect.MainActivity
import com.example.aupairconnect.Screens
import com.example.aupairconnect.graphs.Graph
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import kotlinx.coroutines.*
import java.util.*


class AuthViewModel constructor(
    private val onNavigation: NavHostController,
    private val authRepository: AuthRepository,
) : ViewModel() {

    // TODO: Put variables in viewModel
    //SIGN IN VARIABLES


    // REGISTER VARIABLES
    var registerEmail = mutableStateOf("")
    val registerPassword = mutableStateOf("")
    val registerConfirmPassword = mutableStateOf("")

    val registerName = mutableStateOf("")
    val registerAge = mutableStateOf("")
    val registerCountryOrigin = mutableStateOf("")
    val registerCurrentLocation = mutableStateOf("City, Country")

    var signInSuccessful = mutableStateOf(false)
    var userNotConfirmedFailure = mutableStateOf(false)
    var userEmail: String = ""

    // LOCATION SERVICE VARIABLES
    lateinit var permissionsManager: PermissionsManager
    var permissionsListener = getPermissionsListener()
    var userCoordinates = LatLng(0.0,0.0)

    val COUNTRY_LIST = arrayOf("Afghanistan","Albania","Algeria","Andorra","Angola","Anguilla","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas",
        "Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","British Virgin Islands","Brunei",
        "Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Central African Republic","Chad","Chile","China","Colombia","Comoros","Congo","Cook Islands","Costa Rica","Cote D Ivoire","Croatia",
        "Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Falkland Islands",
        "Faroe Islands","Fiji","Finland","France","French Polynesia","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guam","Guatemala",
        "Guinea","Guinea-Bissau","Guyana","Haiti","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy","Jamaica","Japan",
        "Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Madagascar",
        "Malawi","Malaysia","Maldives","Mali","Malta","Mauritania","Mauritius","Mexico","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nepal","Netherlands",
        "Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland",
        "Portugal","Puerto Rico","Qatar","Romania","Russia","Rwanda","Saint Kitts and Nevis","Saint Lucia","Saint Vincent","Samoa","San Marino","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone",
        "Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland",
        "Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor-Leste","Togo","Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Uganda","Ukraine","United Arab Emirates",
        "United Kingdom","United States","Uruguay","Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)","Yemen","Zambia","Zimbabwe")

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun getCurrentUser(){
        return Amplify.Auth.getCurrentUser({Log.i("AuthCurrentUser", "Current User is: $it")},{Log.e ("AuthCurrentUser", "Couldn't retrieve current user: ", it) })
    }

    fun signIn(email:String, password:String) {
        println("In viewModel")

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO){
                val result = Amplify.Auth.signIn(email, password,
                    { result ->
                        if (result.isSignedIn) {
                            Log.i("AuthQuickstart", "Sign in succeeded")
                            signInSuccessful.value = true
                        } else {
                            Log.i("AuthQuickstart", "Sign in not complete")
                            signInSuccessful.value = false
                        }
                    },
                    {error ->
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
                )
                println("Finished auth, onto next screen")
                delay(2000)
                withContext(Dispatchers.Main){
                    if(signInSuccessful.value){
                        onNavigation.popBackStack()
                        onNavigation.navigate(Graph.HOME)
                    }
                    if(userNotConfirmedFailure.value){
                        println("It pushes to verify screen")
                        userEmail = email
                        val route = Screens.VerifyScreen.route
                        onNavigation.navigate(route)
                    }
                }
            }
        }
    }

    fun registerUser(){
        if(registerEmail.value.isNotBlank()
            && registerPassword.value.isNotBlank()
            && registerConfirmPassword.value.isNotBlank()) {
            if (registerName.value.isNotBlank()
                && registerAge.value.isNotBlank()
                && registerCountryOrigin.value.isNotBlank()
                && registerCurrentLocation.value.isNotBlank()){
                if (registerAge.value.contains(",")
                    || registerAge.value.contains(".")
                    || registerAge.value.contains("-")
                    || registerAge.value.contains(" ")){
                    authRepository.registerUser(registerEmail.value, registerPassword.value)
                } else {
                    //TODO: Add error message
                }
            } else {
                //TODO: Add error message
            }
        } else {
            //TODO: Add error message
        }
    }

    fun confirmSignUp(code:String){
        Amplify.Auth.confirmSignUp(userEmail, code,
            { result ->
                if(result.isSignUpComplete){
                    Log.i("Auth Confirm Signup", "Confirmation Succeeded")
                }
        },
            {Log.e("Auth Confirm SignUp", "Confirmation Failed")}
        )
    }

    fun resendVerificationCode(){
        Amplify.Auth.resendSignUpCode(userEmail,
        { Log.i("AuthDemo", "Code was sent again: $it") }, {Log.e("AuthDemo", "Failed to resend code", it) })
    }

    fun goToSignUpScreen(){
        val route = Screens.RegisterScreen.route
        onNavigation.navigate(route)
    }

    fun goToSignInScreen(){
        val route = Screens.LoginScreen.route
        onNavigation.navigate(route)
    }

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
//        locationEngine = LocationEngineProvider.getBestLocationEngine(context)
        println("Checking current location")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (checkPermissions(context)){
            println("Just checked permissions")
            if(isLocationEnabled(context)){
                println("Just saw location enabled is true")
                var cityName: String = "City, Country"
                fusedLocationProviderClient.getCurrentLocation(100, null).addOnCompleteListener { task ->
                    println("We got it boyz!!!")
                    val location: Location? = task.result

                    if(location != null){
                        userCoordinates = LatLng(location.latitude, location.longitude)
                        println("The user's position is: ${userCoordinates.latitude} and ${userCoordinates.longitude}")

                        val geoCoder = Geocoder(context, Locale.getDefault())
                        val address = geoCoder.getFromLocation(location.latitude, location.longitude,3)//getFromLocation(lat,long,1)
                        if(address != null){
                            if(address[0] != null) {
                                userCoordinates = LatLng(address[0].latitude, address[0].longitude)
                                println("${address[0].locality}, ${address[0].countryName}")
                                println(address.toString())
                                registerCurrentLocation.value = "${address[0].locality}, ${address[0].countryName}"
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
            println("check permissions is true")
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
        println("check permissions is FALSE")
        return false
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }

    private fun requestPermission(context: Context){
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }

}
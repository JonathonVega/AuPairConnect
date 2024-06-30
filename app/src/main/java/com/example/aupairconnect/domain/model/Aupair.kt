package com.example.aupairconnect.domain.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.amplifyframework.core.Resources
import com.example.aupairconnect.MyAmplifyApp.Companion.appContext
import com.example.aupairconnect.R
import java.io.File
import java.time.LocalDate
import java.time.Period

data class Aupair(
    //TODO: Already using User class, change content of this class or delete it
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val handle: String = "",
    val status: String = "",
    val birthdate: LocalDate? = LocalDate.now(), //
    val bio: String = "",
    val email: String = "",
    val nationality: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val zipcode: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val chatroomsIds: List<String> = emptyList(),
    val blockedUsers: List<String> = emptyList(),
    val token: String = "",
    val active: Boolean = false,

    var profileImages: ArrayList<Bitmap> = arrayListOf(BitmapFactory.decodeResource(appContext.resources, R.drawable.aupairphoto3))
){
    fun getUserAge(): Int{
        if (birthdate != null) {
            return Period.between(
                LocalDate.of(birthdate.year, birthdate.month, birthdate.dayOfMonth),
                LocalDate.now()
            ).years
        } else {
            return 0
        }
    }
}

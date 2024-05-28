package com.example.aupairconnect.domain.model

import com.amplifyframework.core.model.Model
import java.time.temporal.Temporal
import java.util.Date
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

data class User(
    val id: String? = null,
    val handle: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val status: String? = null,
    val birthdate: LocalDate? = null,
    val nationality: String? = null,
    val bio: String? = null,
    val createdAt: LocalDate? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val zipcode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val chatroomsIds: List<String>? = null,
    val blockedUsers: List<String>? = null,
    val active: Boolean = true


) {
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



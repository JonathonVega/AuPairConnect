package com.example.aupairconnect.domain.model

data class Aupair(
    //TODO: Already using User class, change content of this class or delete it
    val name: String? = null,
    val age: Int? = 0,
    val originLocation: String? = null,
    val currentLocation: String? = null,
    val isExAupair: Boolean? = false,
    val bio: String? = null
)

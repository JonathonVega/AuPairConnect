package com.example.aupairconnect.domain.model

import com.amplifyframework.core.model.Model

data class User(
    val name: String? = null,
    val age: Int? = 0,
    val nationality: String? = null,
    val currentLocation: String? = null,
    val status: String? = null,
    val bio: String? = null
) : Model

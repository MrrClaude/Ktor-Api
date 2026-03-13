package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String
)
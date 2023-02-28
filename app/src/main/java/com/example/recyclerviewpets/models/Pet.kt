package com.example.recyclerviewpets.models

data class Pet(
    val id: Long,
    val name: String,
    val age: Int,
    val photo: String,
    val isFavorite: Boolean = false
)

package com.example.filmapp.domain.entity

data class Film (
    val id: Int,
    val title: String,
    val rating: Double,
    val posterUrlPrev: String,
    val year: Int
)

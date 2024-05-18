package com.example.filmapp.domain.entity

data class FilmDetail(
    val title: String,
    val description: String,
    val posterUrl: String,
    val rating: Double,
    val year: Int,
    val shortDescription: String?,
    val genres: List<String>
)

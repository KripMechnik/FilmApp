package com.example.filmapp.data.remote.dto

data class FilmList(
    val items: List<FilmDto>,
    val total: Int,
    val totalPages: Int
)




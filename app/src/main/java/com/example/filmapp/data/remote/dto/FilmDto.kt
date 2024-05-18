package com.example.filmapp.data.remote.dto

import com.example.filmapp.domain.entity.Film

data class FilmDto(
    val countries: List<Country>,
    val genres: List<Genre>,
    val imdbId: String,
    val kinopoiskId: Int,
    val nameEn: String,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val ratingImdb: Double,
    val ratingKinopoisk: Double,
    val type: String,
    val year: Int
)

fun FilmDto.toFilm(): Film{
    return Film(
        id = kinopoiskId,
        title = nameRu,
        rating = ratingKinopoisk,
        posterUrlPrev = posterUrlPreview,
        year = year
    )
}
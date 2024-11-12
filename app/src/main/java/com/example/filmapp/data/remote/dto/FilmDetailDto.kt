package com.example.filmapp.data.remote.dto

import com.example.filmapp.domain.entity.FilmDetail

data class FilmDetailDto(
    val completed: Boolean,
    val countries: List<CountryX>,
    val coverUrl: String,
    val description: String,
    val editorAnnotation: String,
    val endYear: Int,
    val filmLength: Int,
    val genres: List<GenreX>,
    val has3D: Boolean,
    val hasImax: Boolean,
    val imdbId: String,
    val isTicketsAvailable: Boolean,
    val kinopoiskHDId: String,
    val kinopoiskId: Int,
    val lastSync: String,
    val logoUrl: String,
    val nameEn: String,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val productionStatus: String,
    val ratingAgeLimits: String,
    val ratingAwait: Double,
    val ratingAwaitCount: Int,
    val ratingFilmCritics: Double,
    val ratingFilmCriticsVoteCount: Int,
    val ratingGoodReview: Double,
    val ratingGoodReviewVoteCount: Int,
    val ratingImdb: Double,
    val ratingImdbVoteCount: Int,
    val ratingKinopoisk: Double,
    val ratingKinopoiskVoteCount: Int,
    val ratingMpaa: String,
    val ratingRfCritics: Double,
    val ratingRfCriticsVoteCount: Int,
    val reviewsCount: Int,
    val serial: Boolean,
    val shortDescription: String,
    val shortFilm: Boolean,
    val slogan: String,
    val startYear: Int,
    val type: String,
    val webUrl: String,
    val year: Int
)

fun FilmDetailDto.toFilmDetail() : FilmDetail{
    return FilmDetail(
        title = nameRu,
        description = description,
        posterUrl = posterUrl,
        rating = ratingKinopoisk,
        year = year,
        shortDescription = shortDescription,
        genres = genres.map {it.genre},
        id = kinopoiskId

    )
}
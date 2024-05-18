package com.example.filmapp.data.remote

import com.example.filmapp.common.Constants
import com.example.filmapp.data.remote.dto.FilmDetailDto
import com.example.filmapp.data.remote.dto.FilmList
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {
    @GET("/api/v2.2/films")
    suspend fun getFilms(
        @Header("x-api-key") apiKey: String = Constants.API_KEY,
        @Query("countries") country: String = Constants.COUNTRY,
        @Query("order") order: String = "RATING",
        @Query("type") type: String =  "ALL",
        @Query("ratingFrom") ratingFrom: String =  "0",
        @Query("ratingTo") ratingTo: String =  "10",
        @Query("yearFrom") yearFrom: String =  "1000",
        @Query("yearTo") yearTo: String =  "3000",
        @Query("page") page: String =  "1",
    ):FilmList

    @GET("/api/v2.2/films/{id}")
    suspend fun getFilmById(
        @Path("id") id: String,
        @Header("x-api-key") apiKey: String = Constants.API_KEY
    ):FilmDetailDto
}
package com.example.filmapp.domain.repository

import com.example.filmapp.data.remote.dto.FilmDetailDto
import com.example.filmapp.data.remote.dto.FilmList

interface FilmRepository {
    suspend fun getFilms(): FilmList

    suspend fun getFilmDetail(id: String): FilmDetailDto
}
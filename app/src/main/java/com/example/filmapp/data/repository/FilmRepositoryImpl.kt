package com.example.filmapp.data.repository

import com.example.filmapp.data.remote.KinopoiskApi
import com.example.filmapp.data.remote.dto.FilmDetailDto
import com.example.filmapp.data.remote.dto.FilmList
import com.example.filmapp.domain.repository.FilmRepository
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val api: KinopoiskApi
) : FilmRepository {
    override suspend fun getFilms(): FilmList {
        return api.getFilms()
    }

    override suspend fun getFilmDetail(id: String): FilmDetailDto {

        return api.getFilmById(id)
    }
}
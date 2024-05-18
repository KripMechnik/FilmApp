package com.example.filmapp.domain.usecase

import com.example.filmapp.common.Resource
import com.example.filmapp.data.remote.dto.toFilm
import com.example.filmapp.domain.entity.Film
import com.example.filmapp.domain.repository.FilmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFilmsUseCase @Inject constructor(
    private val repository: FilmRepository
) {
    operator fun invoke() : Flow<Resource<List<Film>>> = flow {
        try {
            emit(Resource.Loading<List<Film>>())
            val films = repository.getFilms().items.map { it.toFilm() }
            emit(Resource.Success<List<Film>>(films))
        } catch(e: HttpException) {
            emit(Resource.Error<List<Film>>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<List<Film>>("Couldn't reach server. Check your internet connection."))
        }
    }
}
package com.example.filmapp.domain.usecase

import com.example.filmapp.common.Resource
import com.example.filmapp.data.remote.dto.toFilmDetail
import com.example.filmapp.domain.entity.FilmDetail
import com.example.filmapp.domain.repository.FilmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFilmDetailUseCase @Inject constructor(
    private val repository: FilmRepository
) {
    operator fun invoke(id: String) : Flow<Resource<FilmDetail>> = flow {
        try {
            emit(Resource.Loading<FilmDetail>())
            val film = repository.getFilmDetail(id).toFilmDetail()
            emit(Resource.Success<FilmDetail>(film))
        } catch(e: HttpException) {
            emit(Resource.Error<FilmDetail>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<FilmDetail>("Couldn't reach server. Check your internet connection."))
        }
    }
}
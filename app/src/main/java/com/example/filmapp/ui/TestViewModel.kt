package com.example.filmapp.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.common.Resource
import com.example.filmapp.domain.entity.FilmDetail
import com.example.filmapp.domain.usecase.GetFilmDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val getFilmDetailUseCase: GetFilmDetailUseCase
) : ViewModel() {
    private val _state = mutableStateOf(FilmDetailState())
    val state: State<FilmDetailState> = _state

    init {
        getFilm("301")

    }

    private fun getFilm(id: String) {
        getFilmDetailUseCase(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = FilmDetailState(film = result.data)
                    result.data?.title?.let { Log.i("RRR", it) }
                }
                is Resource.Error -> {
                    _state.value = FilmDetailState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    result.message?.let { Log.i("RRR", it) }
                }
                is Resource.Loading -> {
                    _state.value = FilmDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    inner class FilmDetailState(
        val isLoading: Boolean = false,
        val film: FilmDetail? = null,
        val error: String = ""
    )
}
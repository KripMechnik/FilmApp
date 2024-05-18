package com.example.filmapp.ui.detailed

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
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
class DetailViewModel @Inject constructor(
    private val getFilmDetailUseCase: GetFilmDetailUseCase
) : ViewModel() {
    val state = MutableLiveData(FilmDetailState())
    val id = MutableLiveData("")

    fun getFilm() {
        Log.i("RRR", id.value ?: "")
        getFilmDetailUseCase(id.value ?: "").onEach { result ->
            when (result) {
                is Resource.Success -> {
                    state.value = FilmDetailState(film = result.data)
                    result.data?.title?.let { Log.i("RRR", it) }
                }
                is Resource.Error -> {
                    state.value = FilmDetailState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    result.message?.let { Log.i("RRR", it) }
                }
                is Resource.Loading -> {
                    state.value = FilmDetailState(isLoading = true)
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
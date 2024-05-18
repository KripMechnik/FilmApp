package com.example.filmapp.ui.list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.common.Resource
import com.example.filmapp.domain.entity.Film
import com.example.filmapp.domain.usecase.GetFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getFilmsUseCase: GetFilmsUseCase
) : ViewModel() {

    inner class FilmListState(
        val isLoading: Boolean = false,
        val films: List<Film> = emptyList(),
        val error: String = ""
    )

    val state = MutableLiveData(FilmListState())


    init {
        getFilms()
        //Log.i("RRR", state.value?.films?.size.toString())
    }

    private fun getFilms() {
        getFilmsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    state.value = FilmListState(films = result.data ?: emptyList())
                    //Log.i("RRR", state.value?.films?.size.toString())
                }
                is Resource.Error -> {
                    state.value = FilmListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.e("RRR", state.value?.error ?: "")
                }
                is Resource.Loading -> {
                    state.value = FilmListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}
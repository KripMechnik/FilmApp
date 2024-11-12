package com.example.filmapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.common.Resource
import com.example.filmapp.domain.entity.Film
import com.example.filmapp.domain.usecase.GetFilmsUseCase
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getFilmsUseCase: GetFilmsUseCase,
    private val firebase: GoogleFirebase
) : ViewModel() {

    inner class FilmListState(
        val isLoading: Boolean = false,
        val films: List<Film> = emptyList(),
        val error: String = ""
    )

    val state = MutableLiveData(FilmListState())

    val photoUrl = MutableLiveData<String>()

    init {
        getFilms()
        getPhotoUrl()
    }

    private fun getPhotoUrl(){
        viewModelScope.launch {
            val res = firebase.getSelfUser()
            res?.let {
                if (res.isNotEmpty()){
                    photoUrl.value = res["photoUrl"] as String
                }
            }
        }
    }

    fun checkAuth(): Boolean{
        return firebase.checkAuth()
    }

    private fun getFilms() {
        getFilmsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    state.value = FilmListState(films = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    state.value = FilmListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    state.value = FilmListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}
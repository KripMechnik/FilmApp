package com.example.filmapp.ui.recommendations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.common.Resource
import com.example.filmapp.domain.entity.FilmDetail
import com.example.filmapp.domain.usecase.GetFilmDetailUseCase
import com.example.filmapp.ui.common.UserState
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    private val getFilmDetailUseCase: GetFilmDetailUseCase,
    private val firebase: GoogleFirebase
): ViewModel() {

    init {
        getFilms()
    }

    val state = MutableLiveData<List<Pair<FilmDetail, UserState>>>()

    private fun getFilms() {
        viewModelScope.launch {
            val idsAndUsers = firebase.getFilmsIdsAndUsers()
            val films = mutableListOf<FilmDetail>()
            val users = mutableListOf<UserState>()
            idsAndUsers.forEach{
                getFilmDetailUseCase(it.first).collect{ result ->
                    when (result) {
                        is Resource.Success -> {
                            films += result.data!!
                        }
                        is Resource.Error -> {

                            Log.i("error_recomm", result.message ?: "An unexpected error occured")

                        }
                        is Resource.Loading -> {
                            Log.i("loading", "loading")
                        }
                    }
                }
                val map = firebase.getUser(it.second)

                map?.let { map ->
                    users.add(UserState(username = map["username"] as String, photoUrl = map["photoUrl"] as String, it.second))
                }

            }
            state.value = films.zip(users)
        }

    }
}
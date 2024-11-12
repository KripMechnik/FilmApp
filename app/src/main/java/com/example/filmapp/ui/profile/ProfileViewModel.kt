package com.example.filmapp.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.ui.common.UserState
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebase: GoogleFirebase
): ViewModel() {

    init {
        getSelfUser()
    }

    val state = MutableLiveData<UserState>()

    private fun getSelfUser(){
        viewModelScope.launch {
            val user = firebase.getSelfUser()
            user?.let {
                state.value = UserState(user["username"] as String, user["photoUrl"] as String, user["uid"] as String)
            }

        }
    }

    fun logout(){
        firebase.signOut()
    }

}
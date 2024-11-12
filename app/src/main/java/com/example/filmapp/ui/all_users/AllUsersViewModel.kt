package com.example.filmapp.ui.all_users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.ui.common.UserState
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllUsersViewModel @Inject constructor(
    private val firebase: GoogleFirebase
): ViewModel() {

    val state = MutableLiveData<List<UserState>>()

    init {
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch {
            firebase.getAllUsers()?.let {
                state.value = it
            }
        }
    }

    fun sendInvite(uid: String){
        viewModelScope.launch {
            firebase.sendFriendInvite(uid)
        }
    }
}
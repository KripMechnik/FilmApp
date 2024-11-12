package com.example.filmapp.ui.friends

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.ui.common.UserState
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val firebase: GoogleFirebase
) : ViewModel() {

    init {
        getFriends()
        getNotAcceptedFriends()
    }

    val friendsState = MutableLiveData<List<UserState>>()

    val notAcceptedFriendsState = MutableLiveData<List<UserState>>()

    private fun getFriends(){
        viewModelScope.launch {
            firebase.getFriends()?.let {
                friendsState.value = it
            }
        }
    }

    private fun getNotAcceptedFriends(){
        viewModelScope.launch {
            firebase.getFriendInvites()?.let {
                notAcceptedFriendsState.value = it
            }

        }
    }

    fun acceptFriend(uid: String){
        viewModelScope.launch {
            firebase.acceptInvite(uid)
            firebase.getFriends()?.let {
                friendsState.value = it
            }
            firebase.getFriendInvites()?.let {
                notAcceptedFriendsState.value = it
            }
        }
    }

    fun declineFriend(uid: String){
        viewModelScope.launch {
            firebase.declineInvite(uid)
            firebase.getFriendInvites()?.let {
                notAcceptedFriendsState.value = it
            }
        }
    }

}
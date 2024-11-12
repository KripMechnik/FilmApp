package com.example.filmapp.ui.detailed.bottom_sheet_dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.ui.common.UserState
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsSheetViewModel @Inject constructor(
    private val firebase: GoogleFirebase
) : ViewModel() {

    init {
        getFriends()
    }

    val state = MutableLiveData<List<UserState>>()

    private fun getFriends(){
        viewModelScope.launch {
            firebase.getFriends()?.let {
                state.value = it
            }
        }
    }

    fun sendRecommendation(uidTo: String, id: String){
        viewModelScope.launch {
            firebase.sendRecommendation(uidTo, id)
        }
    }

}
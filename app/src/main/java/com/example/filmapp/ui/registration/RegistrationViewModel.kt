package com.example.filmapp.ui.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.ui.firebase.GoogleFirebase
import com.example.filmapp.ui.firebase.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val firebase: GoogleFirebase
) : ViewModel(){

    val registrationResult = MutableLiveData<Boolean>()

    var state = MutableLiveData<UserData>()
        private set

    fun registerUser(user: UserData){
        viewModelScope.launch {
            registrationResult.value = firebase.register(user)
        }

    }
}
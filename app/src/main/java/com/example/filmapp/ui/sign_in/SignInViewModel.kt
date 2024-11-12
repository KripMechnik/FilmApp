package com.example.filmapp.ui.sign_in

import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.ui.firebase.GoogleFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebase: GoogleFirebase
): ViewModel() {

    val result = MutableLiveData<Boolean>()

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            firebase.signIn(email, password).onSuccess {
                result.value = true
            }.onFailure {
                result.value = false
            }
        }
    }

}
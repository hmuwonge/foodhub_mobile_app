package com.metadent.foodhub_android.ui.features.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.models.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val foodApi: FoodApi): ViewModel() {
    private val _uiState = MutableStateFlow<SignUpEvent>(SignUpEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignupNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    fun onEmailChange(email: String){
        _email.value =email
    }

    fun onPasswordChange(password: String){
        _password.value =password
    }

    fun onNameChange(name: String){
        _name.value =name
    }

    fun onSignUpClick(){

        viewModelScope.launch {
            _uiState.value = SignUpEvent.Loading
            try {
                val response = foodApi.signUp(
                    SignUpRequest(
                        name=name.value,
                        email=email.value,
                        password=password.value
                    ))

                if (response.token.isNotEmpty()){
                    _uiState.value =SignUpEvent.Success
                    _navigationEvent.tryEmit(SignupNavigationEvent.NavigateToHome)
                }
            }catch (e: Exception){
                e.printStackTrace()
                _uiState.value =SignUpEvent.Error
            }

//            //perform signup
//            _uiState.value = SignUpEvent.Success
//            _navigationEvent.tryEmit(SignupNavigationEvent.NavigateToHome)
        }

    }

    sealed class SignupNavigationEvent{
        object NavigateToLogin: SignupNavigationEvent()
        object NavigateToHome: SignupNavigationEvent()
    }

    sealed class SignUpEvent{
        object Nothing: SignUpEvent()
        object Success: SignUpEvent()
        object Error: SignUpEvent()
        object Loading: SignUpEvent()
    }
}
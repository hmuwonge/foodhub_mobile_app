package com.metadent.foodhub_android.ui.features.auth.signUp

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.auth.GoogleAuthUiProvider
import com.metadent.foodhub_android.data.models.SignUpRequest
import com.metadent.foodhub_android.ui.features.auth.BaseAuthViewModel
import com.metadent.foodhub_android.ui.features.auth.signIn.SignInViewModel.SignInEvent
import com.metadent.foodhub_android.ui.features.auth.signIn.SignInViewModel.SignInNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(override val foodApi: FoodApi): BaseAuthViewModel(foodApi) {

    val googleAuthUiProvider = GoogleAuthUiProvider()

    override fun loading() {
        viewModelScope.launch { _uiState.value = SignUpEvent.Loading }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch { _uiState.value = SignUpEvent.Error }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch { _uiState.value = SignUpEvent.Error }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Success
            _navigationEvent.emit(SignUpNavigationEvent.NavigateToHome)
        }
    }

    private val _uiState = MutableStateFlow<SignUpEvent>(SignUpEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignUpNavigationEvent>()
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
                    _navigationEvent.emit(SignUpNavigationEvent.NavigateToHome)
                }
            }catch (e: Exception){
                e.printStackTrace()
                _uiState.value =SignUpEvent.Error
            }

//            //perform signup
//            _uiState.value = SignUpEvent.Success
//            _navigationEvent.tryEmit(SignUpNavigationEvent.NavigateToHome)
        }

    }

//    fun onGoogleSignInClicked(context: Context){
//        viewModelScope.launch {
//            _uiState.value =SignUpEvent.Loading
//
//            val response = googleAuthUiProvider.signIn(context,CredentialManager.create(context))
//
//            if (response != null) {
//                _uiState.value = SignInEvent.Success
//                _navigationEvent.emit(SignInNavigationEvent.NavigateToHome)
//            }else{
//                _uiState.value = SignInEvent.Error
//            }
//            _navigationEvent.emit(SignUpNavigationEvent.NavigateToSignUp)
//        }
//    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SignUpNavigationEvent.NavigateToLogin)
        }
    }

    sealed class SignUpNavigationEvent{
        object NavigateToSignUp: SignUpNavigationEvent()
        object NavigateToLogin: SignUpNavigationEvent()
        object NavigateToHome: SignUpNavigationEvent()
    }


    sealed class SignUpEvent{
        object Nothing: SignUpEvent()
        object Success: SignUpEvent()
        object Error: SignUpEvent()
        object Loading: SignUpEvent()
    }


}
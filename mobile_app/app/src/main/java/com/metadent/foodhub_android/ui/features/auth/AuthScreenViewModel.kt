package com.metadent.foodhub_android.ui.features.auth

import androidx.lifecycle.viewModelScope
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.FoodHubSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthScreenViewModel @Inject constructor(override val foodApi: FoodApi,val session: FoodHubSession): BaseAuthViewModel(foodApi) {


    private val _uiState = MutableStateFlow<AuthEvent>(AuthEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthScreenNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    sealed class AuthScreenNavigationEvent {
        object NavigateToSignUp : AuthScreenNavigationEvent()
        object NavigateToLogin : AuthScreenNavigationEvent()
        object NavigateToHome : AuthScreenNavigationEvent()
        object ShowErrorDialog : AuthScreenNavigationEvent()
    }

    sealed class AuthEvent {
        object Nothing : AuthEvent()
        object Success : AuthEvent()
        object Error : AuthEvent()
        object Loading : AuthEvent()
    }

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = AuthEvent.Loading }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            errorDescription=msg
            error = "Google Sign In Failed"
            _uiState.value = AuthEvent.Error
            _navigationEvent.emit(AuthScreenNavigationEvent.ShowErrorDialog)
        }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch {
            errorDescription=msg
            error = "Facebook Sign In Failed"
            _uiState.value = AuthEvent.Error
            _navigationEvent.emit(AuthScreenNavigationEvent.ShowErrorDialog)
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            session.storeToken(token)
            _uiState.value = AuthEvent.Success
            _navigationEvent.emit(AuthScreenNavigationEvent.NavigateToHome)
        }
    }
}
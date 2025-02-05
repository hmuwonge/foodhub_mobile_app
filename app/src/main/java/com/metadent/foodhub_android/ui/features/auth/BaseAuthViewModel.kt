package com.metadent.foodhub_android.ui.features.auth

import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.auth.GoogleAuthUiProvider
import com.metadent.foodhub_android.data.models.AuthResponse
import com.metadent.foodhub_android.data.models.OAuthRequest
import com.metadent.foodhub_android.data.remote.ApiResponse
import com.metadent.foodhub_android.data.remote.safeApiCall
import kotlinx.coroutines.launch
import java.lang.Error

abstract class BaseAuthViewModel(open val foodApi: FoodApi): ViewModel() {
    private val googleAuthUiProvider = GoogleAuthUiProvider()
    private lateinit var callbackManager: CallbackManager

    abstract fun loading()
    abstract  fun onGoogleError(msg:String)
    abstract fun onFacebookError(msg:String)
    abstract fun onSocialLoginSuccess(token:String)

    fun onFacebookClicked(context: ComponentActivity){
        initiateFacebookLogin(context)
    }

    fun onGoogleClicked(context: ComponentActivity){
        initiateGoogleSignIn(context)
    }

    fun initiateGoogleSignIn(context: androidx.activity.ComponentActivity){
        viewModelScope.launch {
           loading()

            val response = googleAuthUiProvider.signIn(context, CredentialManager.create(context))

            fetchFoodApiToken(response.token,"google"){
                onGoogleError(it)
            }
//            _navigationEvent.emit(SignInNavigationEvent.NavigateToSignUp)
        }
    }

    private fun fetchFoodApiToken(token:String, provider: String, onError: (String)->Unit){
        viewModelScope.launch {
            val request = OAuthRequest(token=token,provider=provider)
            val res = safeApiCall<AuthResponse> {foodApi.oAuth(request)  }

            when (res){
                is ApiResponse.Success->{
                    onSocialLoginSuccess(res.data.token)
                }
                else->{
                    val error = (res as? ApiResponse.Error)?.code
                    if(error != null){
                        when (error){
                            401 -> onError("Invalid token")
                            500 -> onError("Server Error")
                            404 -> onError("Not Found")
                            else -> onError("Failed")
                        }
                    }else{
                        onError("Failed")
                    }
            }

        }

        }
    }

   protected fun initiateFacebookLogin(context: androidx.activity.ComponentActivity){
//        _uiState.value = SignInEvent.Loading
loading()
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // App code
                    fetchFoodApiToken(loginResult.accessToken.token,"facebook"){
                        onFacebookError(it)
                    }
                }

                override fun onCancel() {
                    onFacebookError("Failed not token")
                }

                override fun onError(exception: FacebookException) {
                    onFacebookError("Failed not token")
                }
            })

        LoginManager.getInstance().logInWithReadPermissions(
            context,
            callbackManager,
            listOf("public_profile","email")
        )
    }
}
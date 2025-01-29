package com.metadent.foodhub_android.ui.features.auth

import android.util.Log
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
import com.metadent.foodhub_android.data.models.OAuthRequest
import com.metadent.foodhub_android.ui.features.auth.signIn.SignInViewModel.SignInEvent
import com.metadent.foodhub_android.ui.features.auth.signIn.SignInViewModel.SignInNavigationEvent
import kotlinx.coroutines.launch

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

            if (response != null) {
                val request = OAuthRequest(
                    token = response.token,
                    provider = "google"
                )

                val res =foodApi.oAuth(request)
                if (res.token.isNotEmpty()){
                   onSocialLoginSuccess(res.token)
                }else{
                  onGoogleError("Failed")
                }
            }else{
                onGoogleError("Failed")
            }
//            _navigationEvent.emit(SignInNavigationEvent.NavigateToSignUp)
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
                    viewModelScope.launch {
                        val request = OAuthRequest(
                            token = loginResult.accessToken.token,
                            provider = "facebook"
                        )

                        val res =foodApi.oAuth(request)
                        if (res.token.isNotEmpty()){
                            onSocialLoginSuccess(res.token)
                        }else{
                            onFacebookError("Failed not token")
                        }
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
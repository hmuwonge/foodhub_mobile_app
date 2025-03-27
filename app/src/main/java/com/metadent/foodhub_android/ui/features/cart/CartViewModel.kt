package com.metadent.foodhub_android.ui.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.models.CartItem
import com.metadent.foodhub_android.data.models.CartResponse
import com.metadent.foodhub_android.data.remote.ApiResponse
import com.metadent.foodhub_android.data.remote.safeApiCall
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(val foodApi: FoodApi): ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event

    init {
        getCart()
    }

     fun getCart() {
         viewModelScope.launch {
             _uiState.value= CartUiState.Loading
             val response = safeApiCall { foodApi.getCart() }
             when(response){
                 is ApiResponse.Success->{
                     _uiState.value = CartUiState.Success(response.data)
                 }

                 is ApiResponse.Error->{
                     _uiState.value = CartUiState.Error(response.message)
                 }

                 else -> {
                     _uiState.value = CartUiState.Error("An error occured")
                 }
             }
         }
    }


    fun incrementQuantity(cartItem: CartItem, quantity: Int){

    }

    fun decrementQuantity(cartItem: CartItem, quantity: Int){

    }

    fun checkout()
    {

    }

    fun removeItem(cartItem: CartItem){

    }

    sealed class CartUiState{
        object Nothing : CartUiState()
        object Loading : CartUiState()
        data class Error(val message: String) : CartUiState()
        data class Success(val data: CartResponse) : CartUiState()
    }

    sealed class CartEvent{
        object showErrorDialog: CartEvent()
        object OnCheckout: CartEvent()
    }
}
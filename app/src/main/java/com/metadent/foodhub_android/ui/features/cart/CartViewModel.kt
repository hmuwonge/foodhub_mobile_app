package com.metadent.foodhub_android.ui.features.cart

import androidx.lifecycle.ViewModel
import com.metadent.foodhub_android.data.models.CartItem
import com.metadent.foodhub_android.data.models.CartResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CartViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event

    fun incrementQuantity(cartItem: CartItem, quantity: Int){

    }

    fun decrementQuantity(cartItem: CartItem, quantity: Int){

    }

    fun checkout()
    {

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
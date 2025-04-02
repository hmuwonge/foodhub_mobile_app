package com.metadent.foodhub_android.ui.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.models.CartItem
import com.metadent.foodhub_android.data.models.CartResponse
import com.metadent.foodhub_android.data.models.UpdateCartItemRequest
import com.metadent.foodhub_android.data.remote.ApiResponse
import com.metadent.foodhub_android.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(val foodApi: FoodApi): ViewModel() {

    var errorTitle: String=""
    var errorMessage: String=""
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event

   private var cartResponse: CartResponse? =null

    private val _cartItemCount =MutableStateFlow(0)
    val cartIemCount = _cartItemCount.asStateFlow()

    init {
        getCart()
    }

     fun getCart() {
         viewModelScope.launch {
             _uiState.value= CartUiState.Loading
             val response = safeApiCall { foodApi.getCart() }
             when(response){
                 is ApiResponse.Success->{
                     cartResponse = response.data
                     _cartItemCount.value =response.data.items.size
                     _uiState.value = CartUiState.Success(response.data)
                 }

                 is ApiResponse.Error->{
                     _uiState.value = CartUiState.Error(response.message)
                 }

                 else -> {
                     _uiState.value = CartUiState.Error("An error occurred")
                 }
             }
         }
    }


    fun incrementQuantity(cartItem: CartItem){
        if (cartItem.quantity == 5){
            return
        }
        updateItemQuantity(cartItem, cartItem.quantity + 1)
    }

    fun decrementQuantity(cartItem: CartItem){
        if (cartItem.quantity == 1){
            return
        }
        updateItemQuantity(cartItem, cartItem.quantity-1)
    }

    private fun updateItemQuantity(cartItem: CartItem, quantity: Int){
        viewModelScope.launch {
            val res = safeApiCall {
                foodApi.updateCart(UpdateCartItemRequest(cartItem.id, quantity))
            }
            when(res){
                is ApiResponse.Success->{
                    getCart()
                }

//                is ApiResponse.Error->{
//                    cartResponse?.let {
//                        _uiState.value = CartUiState.Success(cartResponse!!)
//                    }
//                    _event.emit(CartEvent.OnQuantityUpdateError)
//                }

                else -> {
                    cartResponse?.let {
                        _uiState.value = CartUiState.Success(cartResponse!!)
                    }
                    errorTitle="Cannot Update Quantity"
                    errorMessage="An error occurred while updating item quantity"
                    _event.emit(CartEvent.OnQuantityUpdateError)
                }
            }
        }
    }

    fun checkout()
    {

    }

    fun removeItem(cartItem: CartItem){
        viewModelScope.launch {
            val res = safeApiCall {
                foodApi.deleteCartItem(cartItem.id)
            }
            when(res){
                is ApiResponse.Success->{
                    getCart()
                }

//                is ApiResponse.Error->{
//                    cartResponse?.let {
//                        _uiState.value = CartUiState.Success(cartResponse!!)
//                    }
//                }

                else -> {
                    cartResponse?.let {
                        _uiState.value = CartUiState.Success(cartResponse!!)
                    }
                    errorTitle="Cannot Delete Quantity"
                    errorMessage="An error occurred while deleting item quantity"
                    _event.emit(CartEvent.OnItemRemoveError)
                }
            }
        }

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
        object OnQuantityUpdateError: CartEvent()
        object OnItemRemoveError: CartEvent()
    }
}
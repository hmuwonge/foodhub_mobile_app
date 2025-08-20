package com.metadent.foodhub_android.ui.features.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.models.Address
import com.metadent.foodhub_android.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel()
class AddressListViewModel @Inject constructor(val foodApi: FoodApi): ViewModel() {

    private val _state = MutableStateFlow<AddressState>(AddressState.Loading)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddressEvent?>()
    val event = _event.asSharedFlow()

    init {
        getAddress()
    }

    fun getAddress(){
        viewModelScope.launch {
            _state.value = AddressState.Loading
            val result = safeApiCall { foodApi.getUserAddress() }
        }
    }

    sealed class AddressState{
        object Loading: AddressState()
        data class Success(val data: List<Address>): AddressState()
        data class Error(val message: String): AddressState()
    }
    sealed class AddressEvent{
        object NavigateToEditAddress: AddressEvent()
        object NavigateToAddress: AddressEvent()
    }
}
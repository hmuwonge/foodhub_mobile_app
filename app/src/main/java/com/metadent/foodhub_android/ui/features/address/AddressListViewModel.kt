package com.metadent.foodhub_android.ui.features.address

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel()
class AddressListViewModel @Inject constructor(): ViewModel() {

    sealed class AddressState{
        object Loading
    }
}
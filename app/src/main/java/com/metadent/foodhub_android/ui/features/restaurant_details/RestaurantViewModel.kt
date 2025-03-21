package com.metadent.foodhub_android.ui.features.restaurant_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.models.FoodItem
import com.metadent.foodhub_android.data.remote.ApiResponse
import com.metadent.foodhub_android.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(val foodApi: FoodApi): ViewModel() {
    var errorMessage =""
    var errorDescription =""
    private val _uiState = MutableStateFlow<RestaurantEvent>(RestaurantEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RestaurantNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun getFoodItem(id: String){
        viewModelScope.launch {
            _uiState.value =RestaurantEvent.Loading
            try {
                val response = safeApiCall {
                    foodApi.getFoodItemsForRestaurant(id)
                }

                when(response){
                    is ApiResponse.Success->{
                        _uiState.value =RestaurantEvent.Sucess(response.data.foodItems)
                    }
                    else->{
                        val error = (response as? ApiResponse.Error)?.code

                        when(error){
                            401->{
                                errorMessage ="Unauthorized"
                               errorDescription="You are not authorized to view this content"
                            }
                            404->{
                                errorMessage ="Not Found"
                                errorDescription="The Restaurant was not found"
                            }
                            else->{
                                errorMessage ="Error!"
                                errorDescription="An error occured"
                            }
                        }
                        _uiState.value =RestaurantEvent.Error
                        _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
                    }
                }
//                _uiState.value = RestaurantEvent.Sucess(foodItems = response.)
            }catch (e:Exception){
                _uiState.value =RestaurantEvent.Error
            }
        }
    }

    sealed class RestaurantNavigationEvent{
        data object GoBack: RestaurantNavigationEvent()
        data object ShowErrorDialog: RestaurantNavigationEvent()
        data object NavigateToProductDetails: RestaurantNavigationEvent()
    }

    sealed class RestaurantEvent{
        data object Nothing:RestaurantEvent()
        data class Sucess(val foodItems:List<FoodItem>):RestaurantEvent()
        data object Error:RestaurantEvent()
        data object Loading:RestaurantEvent()
    }
}
package com.metadent.foodhub_android.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.models.Category
import com.metadent.foodhub_android.data.models.Restaurant
import com.metadent.foodhub_android.data.remote.ApiResponse
import com.metadent.foodhub_android.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val foodApi: FoodApi):ViewModel() {
    private val _uiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<HomeScreenNavigationEvents?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    var categories = emptyList<Category>()
    var restaurants = emptyList<Restaurant>()

    init {
        viewModelScope.launch {
           categories= getCategories()
           restaurants = getPopularRestaurants()

            if (categories.isNotEmpty() && restaurants.isNotEmpty()){
                _uiState.value =HomeScreenState.Success
            }else{
                _uiState.value = HomeScreenState.Empty
            }
        }
    }

    private suspend fun getCategories():List<Category>{
        var list = emptyList<Category>()
            val response = safeApiCall {
                foodApi.getCategories()
            }

            when (response){
                is ApiResponse.Success->{
                    list =response.data.data
//                    _uiState.value = HomeScreenState.Success
                }

//                is ApiResponse.Error->{
//                    _uiState.value = HomeScreenState.Empty
//                }

                else->{
//                    _uiState.value = HomeScreenState.Empty
                }
            }
return list
    }

   private suspend  fun getPopularRestaurants():List<Restaurant>{
       var list = emptyList<Restaurant>()

        val response = safeApiCall {
            foodApi.getRestaurants(40.7128,-74.0060)
        }

        when (response){
            is ApiResponse.Success->{
                list=response.data.data
//                _uiState.value = HomeScreenState.Success
            }

            else->{
//                _uiState.value = HomeScreenState.Empty
            }
        }

       return list
    }

    fun onRestaurantSelected(it: Restaurant) {
        viewModelScope.launch {
            _navigationEvent.emit(
                HomeScreenNavigationEvents.NavigationToDetail(
                    it.name,
                    it.imageUrl,
                    it.id
                )
            )
        }
    }

    sealed class HomeScreenState{
        object Loading:HomeScreenState()
        object Empty:HomeScreenState()
        object Success:HomeScreenState()
    }

    sealed class HomeScreenNavigationEvents{
        data class NavigationToDetail(
            val name:String,
            val imageUrl:String,
            val id:String
        ):HomeScreenNavigationEvents()
    }
}
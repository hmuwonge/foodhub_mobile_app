package com.metadent.foodhub_android.ui.features.food_item_details

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.data.models.FoodItem
import com.metadent.foodhub_android.ui.features.restaurant_details.RestaurantDetails
import com.metadent.foodhub_android.ui.features.restaurant_details.RestaurantDetailsHeader
import com.metadent.foodhub_android.ui.navigation.Cart
import com.metadent.foodhub_android.ui.widgets.BasicDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.FoodDetailsScreen(
    navController: NavController,
    foodItem: FoodItem,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: FoodDetailsViewModel = hiltViewModel()
){
    val count = viewModel.quality.collectAsStateWithLifecycle()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = remember { mutableStateOf(false) }
    val showErrorDialog =remember { mutableStateOf(false) }
    val showSuccessDialog =remember { mutableStateOf(false) }

    when(uiState.value){
        FoodDetailsViewModel.FoodDetailsUiState.Loading -> {
            isLoading.value =true
        }
        else -> {
            isLoading.value=false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when(it){
                is FoodDetailsViewModel.FoodDetailsEvent.onAddToCart->{
//                    Toast.makeText(
//                        navController.context,
//                        "Item added to cart",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    showSuccessDialog.value=true
                }

                is FoodDetailsViewModel.FoodDetailsEvent.showErrorDialog -> {
//                    Toast.makeText(
//                        navController.context,
//                        it.message,
//                        Toast.LENGTH_SHORT
//                    ).show()

                    showErrorDialog.value =true
                }

                is FoodDetailsViewModel.FoodDetailsEvent.goToCart -> {
                    navController.navigate(Cart)
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RestaurantDetailsHeader(
            imageUrl = foodItem.imageUrl,
            restaurantID = foodItem.id,
            animatedVisibilityScope=animatedVisibilityScope,
            onBackButton = {navController.popBackStack()}
        ) { }

        RestaurantDetails(
            title = foodItem.name,
            description = foodItem.description,
            restaurantID = foodItem.id,
            animatedVisibilityScope=animatedVisibilityScope
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
            Text(
                text = "USH${foodItem.price}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            FoodItemCounter(
                onCountIncrement = {viewModel.incrementQuantity()},
                onCountDecrement = { viewModel.decrementQuantity() },
                count = count.value
            )
        }


        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(visible = !isLoading.value) {
        Button(
           onClick = {
               viewModel.addToCart(
                   restaurantId = foodItem.restaurantId,
                   foodITemId = foodItem.id
               )
           },
            enabled = !isLoading.value,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Left,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 4.dp)
                    .clip(
                        RoundedCornerShape(32.dp)
                    )
            ) {
                    Image(painter = painterResource(id =R.drawable.cart ),contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text="Add to Cart".uppercase(),
                        style=MaterialTheme.typography.bodyMedium)
            }
        }
        }
        AnimatedVisibility(visible = isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }

    }

    if (showSuccessDialog.value){
        ModalBottomSheet(onDismissRequest = {showSuccessDialog.value=false}) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(text = "Item added to cart",
                    style= MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    showSuccessDialog.value=false
                    viewModel.goToCart()
                },
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                    Text(text = "Go To Cart")
                }

                Button(onClick = {showSuccessDialog.value =false},
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                    Text(text = "Ok")
                }

            }
        }
    }

    if (showErrorDialog.value){
        ModalBottomSheet(onDismissRequest = {showSuccessDialog.value=false}) {
            BasicDialog(title="Error", description = (uiState.value as? FoodDetailsViewModel.FoodDetailsUiState.Error)?.message ?: "Failed") { }
        }
    }

}



@Composable
fun FoodItemCounter(
    onCountIncrement:()->Unit,
    onCountDecrement:()->Unit,
    count: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.add),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onCountIncrement.invoke()
                }
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = "$count",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.size(4.dp))

        Image(
            painter = painterResource(id = R.drawable.minus),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onCountDecrement.invoke()
                })
    }
}
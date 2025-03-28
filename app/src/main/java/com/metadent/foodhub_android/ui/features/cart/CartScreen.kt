package com.metadent.foodhub_android.ui.features.cart

import android.graphics.Color
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.data.models.CartItem
import com.metadent.foodhub_android.data.models.CheckoutDetails
import com.metadent.foodhub_android.ui.features.food_item_details.FoodItemCounter
import com.metadent.foodhub_android.utils.formatCurrency

@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel = hiltViewModel()){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        CartHeaderView(onBack = {
            navController.popBackStack()
        })
        Spacer(modifier = Modifier.size(16.dp))

        when(uiState.value){
            is CartViewModel.CartUiState.Loading -> {
                Spacer(modifier = Modifier.size(16.dp))
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.size(16.dp))
                    CircularProgressIndicator()
                }
            }

            is CartViewModel.CartUiState.Success -> {
                val data = (uiState.value as CartViewModel.CartUiState.Success).data
                LazyColumn {
                    items(data.items){
                        CartItemView(
                            cartItem = it,
                            onIncrement = { cartItem, quantity ->
                                viewModel.incrementQuantity(
                                    cartItem,
                                    quantity
                                )
                            },
                            onDecrement = { cartItem, quantity ->
                                viewModel.decrementQuantity(
                                    cartItem,
                                    quantity
                                )
                            }, onRemove = {
                                viewModel.removeItem(it)
                            }
                        )
                    }
                    item{
                        CheckoutDetailsView(data.checkoutDetails)
                    }
                }
            }

            is CartViewModel.CartUiState.Error -> {
                Column(Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {

                    val message = (uiState.value as CartViewModel.CartUiState.Error).message
                    Text(text = message, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = {}) {
                        Text(text = "Retry")
                    }
                }
            }

            is CartViewModel.CartUiState.Nothing -> {}
        }

        Spacer(modifier = Modifier.weight(1f))
        if (uiState.value is CartViewModel.CartUiState.Success){
            Button(
                onClick = {viewModel.checkout()},
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Checkout")
            }
        }

    }

}

@Composable
fun CheckoutDetailsView(checkoutDetails: CheckoutDetails) {
   Column {
       CheckoutRowItem(
           title = "SubTotal", value = checkoutDetails.subTotal
       )

       HorizontalDivider( modifier = Modifier.padding(vertical = 2.dp),
           color = androidx.compose.ui.graphics.Color.LightGray, thickness = 1.dp
           )
       CheckoutRowItem(
           title = "Tax", value = checkoutDetails.tax
       )
       HorizontalDivider( modifier = Modifier.padding(vertical = 2.dp))
       CheckoutRowItem(
           title = "Delivery Fee", value = checkoutDetails.deliveryFee
       )
       HorizontalDivider( modifier = Modifier.padding(vertical = 2.dp))
       CheckoutRowItem(
           title = "Total", value = checkoutDetails.totalAmount,
       )
   }
}

@Composable
fun CheckoutRowItem(title: String, value: Double){
    Column {

        Row(modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = formatCurrency(value), style = MaterialTheme.typography.titleMedium)
//            Text(text = currency, style = MaterialTheme.typography.titleMedium,
//                color = androidx.compose.ui.graphics.Color.LightGray)
        }
        VerticalDivider()
    }
}

@Composable
fun CartItemView(cartItem: CartItem,
                 onIncrement: (CartItem, Int)->Unit,
                 onDecrement: (CartItem, Int)->Unit,
                 onRemove:(CartItem)->Unit) {

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround) {
        AsyncImage(model = cartItem.menuItemId.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(82.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier= Modifier.size(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text=cartItem.menuItemId.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {onRemove.invoke(cartItem)},
                    modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Filled.Close,contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }

            }
            Text(text = cartItem.menuItemId.description,
                maxLines = 1,
                color = androidx.compose.ui.graphics.Color.Gray
            )

            Row {
                Text(text = "${cartItem.menuItemId.price}",
                    style= MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.weight(1f))
                FoodItemCounter(
                    onCountIncrement = {onIncrement(cartItem, cartItem.quantity)},
                    onCountDecrement = {onDecrement(cartItem, cartItem.quantity) },
                    count = cartItem.quantity
                )
            }

        }
    }
}

@Composable
fun CartHeaderView(onBack: ()-> Unit)
{
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center) {
        IconButton(onClick = onBack) {
            Image(painter = painterResource(id=R.drawable.back), contentDescription = null)
        }
        Text(text = "Cart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.size(8.dp))
    }
}

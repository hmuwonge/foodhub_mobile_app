package com.metadent.foodhub_android.ui.features.cart

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel= hiltViewModel()){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        CartHeaderView(onBack = {
            navController.popBackStack()
        })
            Spacer(modifier = Modifier.size(16.dp))

            when(uiState.value){
                is CartViewModel.CartUiState.Loading->{
                    Spacer(modifier = Modifier.size(16.dp))
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.size(16.dp))
                        CircularProgressIndicator()
                    }
                }

                is CartViewModel.CartUiState.Success->{
                    val data = (uiState.value as CartViewModel.CartUiState.Success).data
                    LazyColumn {
                        items(data.items){
                            CartItemView(cartItem =it)
                        }
                        item{
                            CheckoutDetailsView()
                        }
                    }
                }

                is CartViewModel.CartUiState.Error->{
                    Column(Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {

                    }
                    val message = (uiState.value as CartViewModel.CartUiState.Error).message
                    Text(text = message, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = {}) {
                        Text(text = "Retry")
                    }
                }
            }
        LazyColumn {  }

    }

}

@Composable
fun CheckoutDetailsView() {
    TODO("Not yet implemented")
}

@Composable
fun CartItemView(cartItem: CartItem) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround) {
        AsyncImage(model = cartItem.menuItemId.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(82.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier= Modifier.size(12.dp))
        Column {
            Row {

            }
            Text(text = cartItem.menuItemId.description,
                maxLines = 1,
                color = Color.GRAY
            )

        }
    }
}

@Composable
fun CartHeaderView(onBack: ()-> Unit)
{
    Row {
        IconButton(onClick = onBack) {
            Image(painter = painterResource(id=R.drawable.back), contentDescription = null)
        }
        Text(text = "Cart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.size(8.dp))
    }
}

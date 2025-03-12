package com.metadent.foodhub_android.ui.features.restaurant_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.data.models.FoodItem

@Composable
fun RestaurantDetailsScreen(
    navController: NavController,
    name: String,
    imageUrl:String,
    restaurantID: String,
    viewModel: RestaurantViewModel= hiltViewModel()
){
    LaunchedEffect(restaurantID) {
        viewModel.getFoodItem(restaurantID)
    }
    val uiState = viewModel.uiState.collectAsState()
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        item { 
            RestaurantDetailsHeader(
                imageUrl = imageUrl,
                onBackButton = { navController.popBackStack() },
                onFavouriteButton = {}
            )
        }
        item {
            RestaurantDetails(
                title = name,
                description = "aFDAS"
            )
        }

        when(uiState.value){
            is RestaurantViewModel.RestaurantEvent.Loading->{
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
CircularProgressIndicator()
                        Text(text="Loading...")
                    }
                }
            }

            is RestaurantViewModel.RestaurantEvent.Sucess->{
                val foodItems =(uiState.value as RestaurantViewModel.RestaurantEvent.Sucess).foodItems

               if (foodItems.isNotEmpty()){
                   items(foodItems){
                           foodItem->
                       Text(text = foodItem.name)
                       FoodItemView(foodItem=foodItem)
                   }
               }else{
                   item {
                       Text(text = "No Food Items")
                   }
               }

            }

            is RestaurantViewModel.RestaurantEvent.Error->{
                item {
                    Text(text = "Error")
                }
            }

            RestaurantViewModel.RestaurantEvent.Nothing->{}
        }
    }
}

@Composable
fun RestaurantDetails(title:String, description:String){
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.size(8.dp))
        Row {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "4.5",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "30+",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "View All Reviews",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}

//Restaurant details screen header
@Composable
fun RestaurantDetailsHeader(
    imageUrl: String,
    onBackButton: ()->Unit,
    onFavouriteButton: ()->Unit,
){
    Box(modifier =Modifier.fillMaxWidth()){
        AsyncImage(model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(
                bottomStart = 16.dp, bottomEnd = 16.dp
            )),
            contentScale = ContentScale.Fit
            )
        IconButton(onClick =
            onBackButton
        , modifier = Modifier.padding(16.dp).size(48.dp).align(Alignment.TopStart)) {
            Image(painter = painterResource(id = R.drawable.back), contentDescription = null)
        }

        IconButton(onClick = onFavouriteButton, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(48.dp)) {
            Image(painter = painterResource(id =R.drawable.favourite), contentDescription = null)
        }
    }
}


//Food item view list
@Composable
fun FoodItemView(foodItem: FoodItem){
    Column(modifier = Modifier
        .padding(8.dp)
        .width(154.43.dp)
        .height(216.dp)
        .clip(RoundedCornerShape(16.dp))
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(147.dp)){
            AsyncImage(
                model = foodItem.imageUrl, contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
//                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "$${foodItem.price}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .align(Alignment.TopStart)
            )
//            Button(onClick = {},
//                modifier = Modifier.padding(8.dp)
//                    .align(Alignment.TopEnd)) {
                Image(
                    painter = painterResource(id=R.drawable.favourite),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                        .clip(CircleShape)
                        .align(Alignment.TopEnd)
                )
//            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "4.5",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "(21)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }

        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )

            Text(
                text = "$${foodItem.description}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}


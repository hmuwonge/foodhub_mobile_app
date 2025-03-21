package com.metadent.foodhub_android.ui.features.restaurant_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.data.models.FoodItem
import com.metadent.foodhub_android.ui.navigation.FoodDetails
import com.metadent.foodhub_android.ui.widgets.gridItems

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetailsScreen(
    navController: NavController,
    name: String,
    imageUrl:String,
    restaurantID: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: RestaurantViewModel= hiltViewModel()
){
    LaunchedEffect(restaurantID) {
        viewModel.getFoodItem(restaurantID)
    }
    val uiState = viewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { 
            RestaurantDetailsHeader(
                imageUrl = imageUrl,
                restaurantID =restaurantID,
                animatedVisibilityScope = animatedVisibilityScope,
                onBackButton = { navController.popBackStack() },
                onFavouriteButton = {}
            )
        }
        item {
            RestaurantDetails(
                title = name,
                description = "aFDAS",
                animatedVisibilityScope = animatedVisibilityScope,
                restaurantID = restaurantID
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
                   gridItems(foodItems,2){
                           foodItem->
//                       Text(text = foodItem.name)
                       FoodItemView(foodItem=foodItem, animatedVisibilityScope = animatedVisibilityScope){
                           navController.navigate(FoodDetails(foodItem))
                       }
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetails(title:String, description:String,
                                            restaurantID: String,
                      animatedVisibilityScope: AnimatedVisibilityScope){
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.sharedElement(
                state = rememberSharedContentState(key="title/${restaurantID}"),
                animatedVisibilityScope
            ))
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
                text = "(30+)",
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
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetailsHeader(
    imageUrl: String,
    restaurantID: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackButton: ()->Unit,
    onFavouriteButton: ()->Unit,
){
    Box(
        modifier =Modifier.fillMaxWidth()){
        AsyncImage(model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
                .height(206.dp)
                .sharedElement(state = rememberSharedContentState(key="image/${restaurantID}"),
                    animatedVisibilityScope)
//                .width(323.dp)
                .clip(RoundedCornerShape(
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
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FoodItemView(
    foodItem: FoodItem,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick:(FoodItem)->Unit){
    Column(modifier = Modifier
        .padding(8.dp)
        .width(154.43.dp)
        .height(216.dp)
        .shadow(
            elevation = 16.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Color.Gray.copy(alpha = 0.8f),
            spotColor = Color.Gray.copy(alpha = 0.8f),
        )
        .background(Color.White)
        .clickable{onClick.invoke(foodItem)}
        .clip(RoundedCornerShape(16.dp))
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(147.dp)){
            AsyncImage(
                model = foodItem.imageUrl, contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .sharedElement(state = rememberSharedContentState(key="image/${foodItem.id}"),
                        animatedVisibilityScope),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "USH${foodItem.price}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(8.dp)
//
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp)
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
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp),
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
                maxLines = 1,
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(key="image/${foodItem.id}"),
                        animatedVisibilityScope),
            )

            Text(
                text = foodItem.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}


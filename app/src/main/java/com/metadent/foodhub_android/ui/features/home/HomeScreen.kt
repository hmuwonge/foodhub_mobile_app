package com.metadent.foodhub_android.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.data.models.Category
import com.metadent.foodhub_android.data.models.Restaurant
import com.metadent.foodhub_android.ui.features.auth.signIn.SignInScreen
import com.metadent.foodhub_android.ui.theme.Typography

@Composable
fun HomeScreen(navController: NavController,viewModel: HomeViewModel= hiltViewModel()){

    Column(modifier = Modifier.fillMaxWidth()) {
        val uiState = viewModel.uiState.collectAsState()
        when(uiState.value){
            is HomeViewModel.HomeScreenState.Loading->{
                Text(text = "Loading")
            }

            is HomeViewModel.HomeScreenState.Empty->{
                Text(text = "Empty")
            }

            is HomeViewModel.HomeScreenState.Success->{
                val categories = viewModel.categories
                CategoriesList(categories=categories, onCategorySelected = {
                    navController.navigate("category/${it.id}")
                })

                RestaurantList(restaurants = viewModel.restaurants, onRestaurantSelected = {
//                    navController.navigate("category/${it.id}")
                })
            }
        }
    }

}

@Composable
fun CategoriesList(categories: List<Category>, onCategorySelected: (Category)->Unit){
    LazyRow {
        items(categories){
            CategoryItem(category = it, onCategorySelected = onCategorySelected)
        }
    }
}

@Composable
fun RestaurantList(restaurants: List<Restaurant>, onRestaurantSelected: (Restaurant)->Unit){
    Column {
        Row {
            Text(
                text = "Popular Restaurants",
                style = Typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    LazyRow {
        items(restaurants){
            RestaurantItem(it,onRestaurantSelected)
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant,onRestaurantSelected: (Restaurant) -> Unit)
{
    Box(
        modifier = Modifier.width(226.dp)
            .height(229.dp)
            .clip(RoundedCornerShape(16.dp))
    ){
        Row(modifier = Modifier.align(Alignment.TopStart)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)) {
            Text(
                text = "4.5",
                style = Typography.bodySmall,
                color = Color.White,
                modifier = Modifier.background(Color.Black)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.size(4.dp))
            Image(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Color.Yellow)
            )
            Text(
                text = "(25)",
                style = Typography.bodySmall,
                color = Color.Gray
            )

        }
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .weight(7.5f),
                contentScale = ContentScale.Inside
            )
            Column(
                modifier = Modifier
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp)
                    .weight(2.5f)
                    .background(Color.White)
                    .clickable { onRestaurantSelected(restaurant) }
            ){
                Text(text = restaurant.name,
                    style= TextStyle(fontSize = 12.sp),
                    color=Color.Black
                )

                Row(){
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.delivery),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Free Delivery",
                            style = Typography.bodySmall,
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.time),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "10-14 mins",
                            style = Typography.bodySmall,
                            color = Color.LightGray
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, onCategorySelected: (Category) -> Unit){

    Column(modifier = Modifier.padding(8.dp)
        .clip(RoundedCornerShape(45.dp))
        .height(90.dp)
        .clickable { onCategorySelected(category) }
        .padding(8.dp)
    ) {
        AsyncImage(model = category.imageUrl, contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape),
            contentScale = ContentScale.Inside)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = category.name)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen()
{
    HomeScreen(rememberNavController())
}

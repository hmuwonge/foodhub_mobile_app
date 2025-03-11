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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
import com.metadent.foodhub_android.ui.navigation.RestaurantDetails
import com.metadent.foodhub_android.ui.theme.Orange
import com.metadent.foodhub_android.ui.theme.Typography
import com.metadent.foodhub_android.ui.theme.Yellow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(navController: NavController,viewModel: HomeViewModel= hiltViewModel()){

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest {
            when(it){
                is HomeViewModel.HomeScreenNavigationEvents.NavigationToDetail -> {
                    navController.navigate(
                        RestaurantDetails(
                            it.id,
                            it.name,
                            it.imageUrl
                        )
                    )
                }
                else->{

                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        val uiState = viewModel.uiState.collectAsState()
        when(uiState.value){
            is HomeViewModel.HomeScreenState.Loading->{
                Box(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                ){
//                    Text(text = "Loading")
                                    CircularProgressIndicator(color = Orange,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                }
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
                    viewModel.onRestaurantSelected(it)
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

            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {}) {
                Text(text="View All", style = Typography.bodySmall)
            }
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
        modifier = Modifier
            .padding(8.dp)
            .width(226.dp)
            .height(229.dp)
            .shadow(16.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
    ){

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color.White)
                    .clickable {onRestaurantSelected(restaurant) }
            ){
                Text(
                    text = restaurant.name,
                    style= Typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.delivery),
                            contentDescription = null,
                            modifier = Modifier.padding(vertical = 8.dp).padding(8.dp).size(12.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "Free Delivery",
                            style = Typography.bodySmall,
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.size(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.time),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "10-14 mins",
                            style = Typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier
            .align(TopStart)
            .padding(8.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "4.5",
                style = Typography.titleSmall,
                color = Color.Black,
                modifier = Modifier
                    .background(Color.White)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Image(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Yellow)
            )
            Text(
                text = "(25)",
                style = Typography.bodySmall,
                color = Color.Gray,
                fontSize = 10.sp
            )

        }
    }
}

@Composable
fun CategoryItem(category: Category, onCategorySelected: (Category) -> Unit){

    Column(
        modifier = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(45.dp))
        .height(90.dp)
        .width(60.dp)
        .clickable { onCategorySelected(category) }

            .shadow(
                12.dp,
                shape = RoundedCornerShape(45.dp),
                ambientColor = Orange,
                spotColor = Orange
            )
        .background(color=Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(model = category.imageUrl, contentDescription = null,
            modifier = Modifier.size(40.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = Orange,
                    spotColor = Orange
                )
                .clip(CircleShape),
            contentScale = ContentScale.Inside)

        Spacer(modifier = Modifier.size(8.dp))
        Text(text = category.name,
            style = TextStyle(fontSize = 10.sp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryItem2(){

    Column(modifier = Modifier
        .padding(8.dp)
        .height(90.dp)
        .clickable {  }
        .padding(8.dp)
        .shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(45.dp),
            ambientColor = Orange,
            spotColor = Orange
        )
        .background(Color.White),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        AsyncImage(model = category.imageUrl, contentDescription = null,
//            modifier = Modifier.size(40.dp).clip(CircleShape),
//            contentScale = ContentScale.Inside)
        Image(
            painter =  painterResource(id=R.drawable.burger),
            contentDescription = null,
            modifier =Modifier.size(40.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = Orange,
                    spotColor = Orange
                )
                .clip(CircleShape),
            contentScale = ContentScale.Crop,

            )
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "category name",
            style = TextStyle(fontSize = 10.sp)
        )
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewCatItem()
{
    CategoryItem2()
}

@Composable
fun RestaurantItem2()
{
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(226.dp)
            .height(229.dp)
            .clip(RoundedCornerShape(16.dp))
    ){

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
//            AsyncImage(
//                model = R.drawable.restaurant,
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize()
//                    .weight(7.5f),
//                contentScale = ContentScale.Inside
//            )

            Image(
              painter =  painterResource(id=R.drawable.restaurant),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .weight(1f),
                contentScale = ContentScale.Crop,

            )
            Column(
                modifier = Modifier
//                    .weight(2.5f)
                    .padding(6.dp)
                    .background(Color.White)
                    .clickable { }
            ){
                Text(
                    text = "sample name",
                    style= Typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row(modifier = Modifier.fillMaxWidth()){
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.delivery),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).size(12.dp)
                        )
                        Spacer(Modifier.size(2.dp))
                        Text(
                            text = "Free Delivery",
                            style = Typography.bodySmall,
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.size(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.time),
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).size(12.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "10-14 mins",
                            style = Typography.bodySmall,
                            color = Color.LightGray
                        )
                    }

                }
            }
        }

        Row(modifier = Modifier
            .align(TopStart)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 1.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "4.5",
                style = Typography.bodySmall,
                color = Color.Black,
                modifier = Modifier
                    .background(Color.White)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Image(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Yellow)
            )
            Text(
                text = "(25)",
                style = Typography.bodySmall,
                color = Color.Gray,
                fontSize = 10.sp
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRestaurantItem(){
    RestaurantItem2()
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen()
{
    HomeScreen(rememberNavController())
}

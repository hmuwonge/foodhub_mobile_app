package com.metadent.foodhub_android.ui.features.food_item_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.data.models.FoodItem
import com.metadent.foodhub_android.ui.features.restaurant_details.RestaurantDetails
import com.metadent.foodhub_android.ui.features.restaurant_details.RestaurantDetailsHeader

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FoodDetailsScreen(
    navController: NavController,
    foodItem: FoodItem,
    animatedVisibilityScope: AnimatedVisibilityScope
){
    val count = remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        RestaurantDetailsHeader(
            imageUrl = foodItem.imageUrl,
            restaurantID = foodItem.restaurantId,
            animatedVisibilityScope=animatedVisibilityScope,
            onBackButton = {}
        ) { }

        RestaurantDetails(
            title = foodItem.name,
            description = foodItem.description,
            restaurantID = foodItem.restaurantId,
            animatedVisibilityScope=animatedVisibilityScope
        )

        Row(modifier = Modifier.fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = "USH${foodItem.price}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.add),
                    contentDescription = null)

                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "${count.value}",
                    style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.size(8.dp))

                Image(painter = painterResource(id = R.drawable.minus),
                    contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
           onClick = {},
            modifier = Modifier.padding(16.dp)
        ) {
            Row( modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(32.dp))) {
                Image(painter = painterResource(id =R.drawable.cart ),contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text="Add to Cart".uppercase(),
                    style=MaterialTheme.typography.bodyMedium)
                
            }
        }

    }

}
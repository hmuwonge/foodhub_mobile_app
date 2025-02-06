package com.metadent.foodhub_android.ui.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.metadent.foodhub_android.data.models.Category

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
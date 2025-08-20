package com.metadent.foodhub_android.ui.features.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.ui.features.cart.AddressCard


@Composable
fun AddressListScreen(navController: NavController,
                      viewModel: AddressListViewModel= hiltViewModel()
){
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest {
            when(val addressEvent=it){
                is AddressListViewModel.AddressEvent.NavigateToEditAddress->{

                }
                is AddressListViewModel.AddressEvent.NavigateToAddress->{}
                else -> {}
            }
        }
    }

    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(painter = painterResource(id=R.drawable.back),
                    contentDescription = null)
            }

            Text(text = "Address List", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.size(40.dp))
        }

        when (val addressState =state.value){
            is AddressListViewModel.AddressState.Loading->{
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray)
                }
            }

            is AddressListViewModel.AddressState.Success->{
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(addressState.data) {address->
                        AddressCard(address=address, onAddressClicked = {})
                    }
                }
            }

            is AddressListViewModel.AddressState.Error->{
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = addressState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray)

                    Button(onClick = {viewModel.getAddress()}) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }

    when (val addressState = state.value){
        is AddressListViewModel.AddressState.Loading->{

        }

        is AddressListViewModel.AddressState.Success->{

        }
        is AddressListViewModel.AddressState.Error->{

        }
    }
}
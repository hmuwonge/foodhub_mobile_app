package com.metadent.foodhub_android

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.metadent.foodhub_android.data.FoodApi
import com.metadent.foodhub_android.data.FoodHubSession
import com.metadent.foodhub_android.data.models.FoodItem
import com.metadent.foodhub_android.ui.features.auth.AuthScreen
import com.metadent.foodhub_android.ui.features.auth.signIn.SignInScreen
import com.metadent.foodhub_android.ui.features.auth.signUp.SignUpScreen
import com.metadent.foodhub_android.ui.features.cart.CartScreen
import com.metadent.foodhub_android.ui.features.cart.CartViewModel
import com.metadent.foodhub_android.ui.features.food_item_details.FoodDetailsScreen
import com.metadent.foodhub_android.ui.features.home.HomeScreen
import com.metadent.foodhub_android.ui.features.restaurant_details.RestaurantDetailsScreen
import com.metadent.foodhub_android.ui.navigation.AddressList
import com.metadent.foodhub_android.ui.navigation.Auth
import com.metadent.foodhub_android.ui.navigation.Cart
import com.metadent.foodhub_android.ui.navigation.FoodDetails
import com.metadent.foodhub_android.ui.navigation.Home
import com.metadent.foodhub_android.ui.navigation.Login
import com.metadent.foodhub_android.ui.navigation.NavRoutes
import com.metadent.foodhub_android.ui.navigation.Notification
import com.metadent.foodhub_android.ui.navigation.RestaurantDetails
import com.metadent.foodhub_android.ui.navigation.SignUp
import com.metadent.foodhub_android.ui.navigation.foodItemNavType
import com.metadent.foodhub_android.ui.theme.FoodHubAndroidTheme
import com.metadent.foodhub_android.ui.theme.Yellow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var showSplashScreen =true

    @Inject
    lateinit var foodApi: FoodApi

    @Inject
    lateinit var session: FoodHubSession

    sealed class BottomNavItem(val route: NavRoutes, val icon: Int){
        object Home: BottomNavItem(com.metadent.foodhub_android.ui.navigation.Home, R.drawable.home_icon)
        object Cart: BottomNavItem(com.metadent.foodhub_android.ui.navigation.Cart, R.drawable.ic_cart)
        object Notification: BottomNavItem(com.metadent.foodhub_android.ui.navigation.Notification, R.drawable.ic_notification)
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                showSplashScreen
            }
            setOnExitAnimationListener{screen->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0f
                )

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0f
                )

                zoomX.duration=500
                zoomY.duration=500
                zoomX.interpolator =OvershootInterpolator()
                zoomY.interpolator =OvershootInterpolator()
                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomY.start()
                zoomX.start()
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodHubAndroidTheme {
                val shouldShowBottomNav= remember { mutableStateOf(false) }
                val navItems = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Cart,
                    BottomNavItem.Notification,
                )

                val navController = rememberNavController();
                val cartViewModel: CartViewModel= hiltViewModel()
                val cartItemCount = cartViewModel.cartIemCount.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination

                        AnimatedVisibility(visible = shouldShowBottomNav.value) {
                            NavigationBar(
                                containerColor = Color.White
                            ) {
                                navItems.forEach { item->
                                    val selected=currentRoute?.hierarchy?.any{
                                        it.route==item.route::class.qualifiedName
                                    }==true
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = {
                                            navController.navigate(item.route)
                                        }, icon = {
                                            Box(modifier = Modifier.size(48.dp)){
                                                Icon(
                                                    painter = painterResource(id = item.icon),
                                                    contentDescription = null,
                                                    tint=if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
                                                    modifier = Modifier.align(Center)
                                                )

                                                if (item.route ==Cart){

                                                    Box(modifier = Modifier.size(16.dp)
                                                        .clip(CircleShape)
                                                        .background(Yellow)
                                                        .align(Alignment.TopEnd)){

                                                        Text(text = "${cartItemCount.value}",
                                                            modifier = Modifier
                                                                .align(Center),
                                                            color = Color.White,
                                                            style = TextStyle(fontSize = 10.sp))
                                                    }
                                                }

                                            }
                                        })
                                }
                            }
                        }

                    }) { innerPadding ->

                    SharedTransitionLayout {
                        NavHost(navController=navController,
                            startDestination = if (session.getToken() !=null) Home else Auth,
                            modifier = Modifier.padding(innerPadding),
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                )+ fadeIn(animationSpec = tween(300))
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                )+ fadeOut(animationSpec = tween(300))
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                )+ fadeIn(animationSpec = tween(300))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                )+ fadeOut(animationSpec = tween(300))
                            }
                        ){
                            composable<SignUp> {
                                shouldShowBottomNav.value=false
                                SignUpScreen(navController)
                            }

                            composable<Auth> {
                                shouldShowBottomNav.value=false
                                AuthScreen(navController)
                            }

                            composable<Login>(

                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                        animationSpec = tween(300)
                                    )+ fadeIn(animationSpec = tween(300))
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                        animationSpec = tween(300)
                                    )+ fadeOut(animationSpec = tween(300))
                                },
                                popEnterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                        animationSpec = tween(300)
                                    )+ fadeIn(animationSpec = tween(300))
                                },
                                popExitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                        animationSpec = tween(300)
                                    )+ fadeOut(animationSpec = tween(300))
                                }
                            ) {
                                shouldShowBottomNav.value=false
                                SignInScreen(navController)
                            }

                            composable<Home> {
                                shouldShowBottomNav.value=true
                                HomeScreen(navController,this)
                            }

                            composable<RestaurantDetails> {
                                shouldShowBottomNav.value=true
                                val route = it.toRoute<RestaurantDetails>()
                                RestaurantDetailsScreen(
                                    navController,
                                    name = route.restaurantName,
                                    imageUrl = route.restaurantImageUrl,
                                    restaurantID = route.restaurantId,
                                    this
                                )
                            }

                            composable<FoodDetails>(typeMap = mapOf(typeOf<FoodItem>() to foodItemNavType)){
                                shouldShowBottomNav.value=true
                                val route =it.toRoute<FoodDetails>()

                                FoodDetailsScreen(navController, foodItem = route.foodItem,
                                    this, onItemAddedToCart = {cartViewModel.getCart()})
                            }

                            composable<Cart>() {
                                shouldShowBottomNav.value=true
                                CartScreen(navController, cartViewModel)
                            }

                            composable<Notification>{
                                shouldShowBottomNav.value=true
                                Box{}
                            }

                            composable<AddressList>{
                                shouldShowBottomNav.value=true
                                AddressScreen()
                            }
                        }
                    }
                }
            }
        }

        if (::foodApi.isInitialized){
            Log.d("MainActivity", "FoodApi initialized")
        }
        CoroutineScope(Dispatchers.IO).launch{
            delay(3000)
            showSplashScreen =false
        }
    }
}
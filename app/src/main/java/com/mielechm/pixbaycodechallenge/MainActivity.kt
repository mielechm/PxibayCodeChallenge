package com.mielechm.pixbaycodechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mielechm.pixbaycodechallenge.features.searchimages.SearchImagesScreen
import com.mielechm.pixbaycodechallenge.ui.theme.PixbayCodeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PixbayCodeChallengeTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "search_images"
                ) {
                    composable("search_images") {
                        SearchImagesScreen(navController = navController)
                    }
                }

            }
        }
    }
}

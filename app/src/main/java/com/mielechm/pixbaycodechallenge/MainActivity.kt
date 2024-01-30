package com.mielechm.pixbaycodechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mielechm.pixbaycodechallenge.features.imagedetails.ImageDetailsScreen
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
                    navController = navController, startDestination = "search_images_screen"
                ) {
                    composable("search_images_screen") {
                        SearchImagesScreen(navController = navController)
                    }
                    composable(
                        "image_details_screen/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) {
                        val id = remember {
                            val id = it.arguments?.getInt("id")
                            id ?: 0
                        }
                        ImageDetailsScreen(id = id, navController = navController)
                    }
                }

            }
        }
    }
}

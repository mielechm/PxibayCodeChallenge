package com.mielechm.pixbaycodechallenge.features.imagedetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ImageDetailsScreen(id: Int, navController: NavController) {
    Text(text = "Image with ID: $id")
}
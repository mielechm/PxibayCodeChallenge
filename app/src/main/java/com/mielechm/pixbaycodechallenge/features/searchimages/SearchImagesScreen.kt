package com.mielechm.pixbaycodechallenge.features.searchimages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.mielechm.pixbaycodechallenge.data.model.ImageListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchImagesScreen(navController: NavController) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Search Images") })
        }) {
            Column(modifier = Modifier.padding(it)) {
                SearchImagesList(navController)
            }
        }
    }
}

@Composable
fun SearchImagesList(
    navController: NavController,
    viewModel: SearchImagesViewModel = hiltViewModel()
) {

    val images by viewModel.images.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val end by viewModel.endReached.collectAsState()

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (images.size % 2 == 0) {
            images.size / 2
        } else {
            images.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !end && !isLoading) {
                LaunchedEffect(key1 = true) {
                    viewModel.searchImagesPaginated()
                }
            }
            ImagesRow(rowIndex = it, images = images, navController = navController)
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (loadError.isNotEmpty()) {
            Text(
                text = loadError,
                modifier = Modifier.padding(20.dp)
            )
        }
    }

}

@Composable
fun ImageItem(image: ImageListItem, navController: NavController, modifier: Modifier = Modifier) {
    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        GoToDetailsDialog(
            onConfirm = {
                openAlertDialog.value = false
                navController.navigate("image_details_screen/${image.id}")
            },
            onDismiss = {
                openAlertDialog.value = false
            })
    }

    Box(
        contentAlignment = Alignment.Center, modifier = modifier.clickable(
            onClick = { openAlertDialog.value = true })
    ) {
        Column {
            SubcomposeAsyncImage(
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                model = image.previewUrl,
                contentDescription = image.tags,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "User: ${image.user}",
                fontSize = 16.sp,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Tags: ${image.tags}",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ImagesRow(rowIndex: Int, images: List<ImageListItem>, navController: NavController) {
    Column {
        Row {
            ImageItem(
                image = images[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (images.size >= rowIndex * 2 + 2) {
                ImageItem(
                    image = images[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun GoToDetailsDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "No")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text(text = "Yes")
            }

        },
        text = { Text(text = "Would you like to see details of this image?") },
        icon = { Icons.Default.Info }
    )

}

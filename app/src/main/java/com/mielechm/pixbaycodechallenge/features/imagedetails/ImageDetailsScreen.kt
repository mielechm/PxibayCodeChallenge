package com.mielechm.pixbaycodechallenge.features.imagedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.mielechm.pixbaycodechallenge.data.entities.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailsScreen(
    id: Int,
    navController: NavController,
    viewModel: ImageDetailsViewModel = hiltViewModel()
) {

    val image by viewModel.imageDetails.collectAsState()

    viewModel.getImageDetails(id)

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(image.tags) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }) {
            Column(modifier = Modifier.padding(it)) {
                ImageDetailsData(image)
            }
        }
    }
}

@Composable
fun ImageDetailsData(image: Image) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(10.dp)) {
        SubcomposeAsyncImage(
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            model = image.largeImageUrl,
            contentDescription = image.tags,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "User: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = image.user)
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Tags: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = image.tags)
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Likes: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = "${image.likes}")
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Downloads: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = "${image.downloads}")
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(text = "Comments: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text = "${image.comments}")
                }
            }, modifier = Modifier.padding(8.dp),
            fontSize = 18.sp
        )

    }
}
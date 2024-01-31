package com.mielechm.pixbaycodechallenge.features.imagedetails

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mielechm.pixbaycodechallenge.data.entities.Image
import com.mielechm.pixbaycodechallenge.repositories.FakeImagesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ImageDetailsViewModelTest {

    private lateinit var viewModel: ImageDetailsViewModel

    @Before
    fun setup() {
        viewModel = ImageDetailsViewModel(FakeImagesRepository())
    }

    @Test
    fun `send request with correct id, returns image`() = runBlocking {
        viewModel.getImageDetails(11)
        viewModel.imageDetails.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(
                Image(
                    id = 11,
                    previewUrl = "url.for.preview",
                    user = "FakeUsername11",
                    tags = "tag1, tag2, tag3",
                    largeImageUrl = "url.for.large",
                    likes = 11,
                    downloads = 2,
                    comments = 3
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

}
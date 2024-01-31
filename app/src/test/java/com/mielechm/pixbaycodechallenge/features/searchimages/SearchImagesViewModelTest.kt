package com.mielechm.pixbaycodechallenge.features.searchimages

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mielechm.pixbaycodechallenge.TestDispatchers
import com.mielechm.pixbaycodechallenge.data.model.ImageListItem
import com.mielechm.pixbaycodechallenge.repositories.FakeImagesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchImagesViewModelTest {

    private lateinit var viewModel: SearchImagesViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setup() {
        testDispatchers = TestDispatchers()
        viewModel = SearchImagesViewModel(testDispatchers, FakeImagesRepository())
    }

    @Test
    fun `calling searchImagePaginated, returns correct response`() = runBlocking {
        viewModel.searchImagesPaginated()
        viewModel.images.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(listOf<ImageListItem>())
            cancelAndConsumeRemainingEvents()
        }

    }
}
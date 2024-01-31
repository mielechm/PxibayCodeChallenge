package com.mielechm.pixbaycodechallenge.repositories

import com.mielechm.pixbaycodechallenge.data.entities.Image
import com.mielechm.pixbaycodechallenge.data.remote.responses.ImagesResponse
import com.mielechm.pixbaycodechallenge.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeImagesRepository : ImagesRepository {

    private val fakeImages = mutableListOf(
        Image(
            id = 1,
            previewUrl = "url.for.preview",
            user = "FakeUsername1",
            tags = "tag1, tag2, tag3",
            largeImageUrl = "url.for.large",
            likes = 1,
            downloads = 2,
            comments = 3
        ),
        Image(
            id = 2,
            previewUrl = "url.for.preview",
            user = "FakeUsername2",
            tags = "tag1, tag2, tag3",
            largeImageUrl = "url.for.large",
            likes = 1,
            downloads = 2,
            comments = 3
        ),
        Image(
            id = 3,
            previewUrl = "url.for.preview",
            user = "FakeUsername3",
            tags = "tag1, tag2, tag3",
            largeImageUrl = "url.for.large",
            likes = 1,
            downloads = 2,
            comments = 3
        )
    )

    private val fakeImagesFlow = MutableStateFlow(fakeImages)
    private val fakeImageFlow = MutableStateFlow(
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

    override suspend fun searchImagesPaginated(
        query: String,
        page: Int,
        perPage: Int
    ): Resource<ImagesResponse> =
        Resource.Success(ImagesResponse(hits = emptyList(), total = 0, totalHits = 0))


    override suspend fun insertImage(image: Image) {
        fakeImages.add(image)
    }

    override fun getAllImages(): Flow<List<Image>> = fakeImagesFlow


    override fun getImageById(id: Int): Flow<Image> = fakeImageFlow

}
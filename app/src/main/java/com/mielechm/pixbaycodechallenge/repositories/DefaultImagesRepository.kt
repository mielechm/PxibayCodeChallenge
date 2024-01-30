package com.mielechm.pixbaycodechallenge.repositories

import com.mielechm.pixbaycodechallenge.data.ImagesDao
import com.mielechm.pixbaycodechallenge.data.entities.Image
import com.mielechm.pixbaycodechallenge.data.remote.PixbayApi
import com.mielechm.pixbaycodechallenge.data.remote.responses.ImagesResponse
import com.mielechm.pixbaycodechallenge.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class DefaultImagesRepository @Inject constructor(
    private val api: PixbayApi,
    private val dao: ImagesDao
) : ImagesRepository {
    override suspend fun searchImagesPaginated(
        query: String,
        page: Int,
        perPage: Int
    ): Resource<ImagesResponse> {
        val response = try {
            api.searchImagesPaginated(query, page, perPage)
        } catch (e: Exception) {
            return Resource.Error(message = "Error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }

    override suspend fun insertImage(image: Image) {
        dao.insert(image)
    }

    override fun getAllImages(): Flow<List<Image>> = dao.getAllImages()

    override fun getImageById(id: Int): Flow<Image> = dao.getImageById(id)

}
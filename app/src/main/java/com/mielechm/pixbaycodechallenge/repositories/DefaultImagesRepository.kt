package com.mielechm.pixbaycodechallenge.repositories

import com.mielechm.pixbaycodechallenge.data.remote.PixbayApi
import com.mielechm.pixbaycodechallenge.data.remote.responses.ImagesResponse
import com.mielechm.pixbaycodechallenge.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultImagesRepository @Inject constructor(private val api: PixbayApi) : ImagesRepository {
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
}
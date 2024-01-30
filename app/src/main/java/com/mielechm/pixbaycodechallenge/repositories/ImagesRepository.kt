package com.mielechm.pixbaycodechallenge.repositories

import com.mielechm.pixbaycodechallenge.data.entities.Image
import com.mielechm.pixbaycodechallenge.data.remote.responses.ImagesResponse
import com.mielechm.pixbaycodechallenge.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {

    suspend fun searchImagesPaginated(query: String, page: Int, perPage: Int): Resource<ImagesResponse>

    suspend fun insertImage(image: Image)

    fun getAllImages(): Flow<List<Image>>

    fun getImageById(id: Int): Flow<Image>
}
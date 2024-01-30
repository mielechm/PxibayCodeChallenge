package com.mielechm.pixbaycodechallenge.repositories

import com.mielechm.pixbaycodechallenge.data.remote.responses.ImagesResponse
import com.mielechm.pixbaycodechallenge.utils.Resource

interface ImagesRepository {

    suspend fun searchImagesPaginated(query: String, page: Int, perPage: Int): Resource<ImagesResponse>
}
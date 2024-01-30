package com.mielechm.pixbaycodechallenge.data.remote

import com.mielechm.pixbaycodechallenge.data.remote.responses.ImagesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixbayApi {

    @GET(".")
    suspend fun searchImagesPaginated(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ImagesResponse
}
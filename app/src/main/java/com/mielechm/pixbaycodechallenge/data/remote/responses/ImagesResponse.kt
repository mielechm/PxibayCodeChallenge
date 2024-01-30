package com.mielechm.pixbaycodechallenge.data.remote.responses

data class ImagesResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)
package com.mielechm.pixbaycodechallenge.data.model

data class ImageListItem(
    val id: Int,
    val previewUrl: String,
    val user: String,
    val tags: String,
    val largeImageUrl: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int
)
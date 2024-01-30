package com.mielechm.pixbaycodechallenge.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey
    val id: Int = 0,
    val previewUrl: String = "",
    val user: String = "",
    val tags: String = "",
    val largeImageUrl: String = "",
    val likes: Int = 0,
    val downloads: Int = 0,
    val comments: Int = 0
)
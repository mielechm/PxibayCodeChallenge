package com.mielechm.pixbaycodechallenge.utils

import com.mielechm.pixbaycodechallenge.data.entities.Image
import com.mielechm.pixbaycodechallenge.data.model.ImageListItem

fun Image.toImageListItem() = ImageListItem(
    id = this.id,
    previewUrl = this.previewUrl,
    user = this.user,
    tags = this.tags,
    largeImageUrl = this.largeImageUrl,
    likes = this.likes,
    downloads = this.downloads,
    comments = this.comments
)

fun ImageListItem.toImage() = Image(
    id = this.id,
    previewUrl = this.previewUrl,
    user = this.user,
    tags = this.tags,
    largeImageUrl = this.largeImageUrl,
    likes = this.likes,
    downloads = this.downloads,
    comments = this.comments
)
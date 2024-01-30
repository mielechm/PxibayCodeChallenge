package com.mielechm.pixbaycodechallenge.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mielechm.pixbaycodechallenge.data.entities.Image

@Database(
    entities = [Image::class], version = 1
)
abstract class ImagesDatabase: RoomDatabase() {

    abstract fun imagesDao(): ImagesDao

    companion object {
        val DATABASE_NAME = "images_db"
    }
}
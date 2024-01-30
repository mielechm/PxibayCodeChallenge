package com.mielechm.pixbaycodechallenge.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mielechm.pixbaycodechallenge.data.entities.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image)

    @Query("SELECT * FROM images")
    fun getAllImages() : Flow<List<Image>>

    @Query("SELECT * FROM images WHERE id=:id")
    fun getImageById(id: Int): Flow<Image>

}
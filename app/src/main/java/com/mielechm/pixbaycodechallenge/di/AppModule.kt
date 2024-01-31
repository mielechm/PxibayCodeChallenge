package com.mielechm.pixbaycodechallenge.di

import android.content.Context
import androidx.room.Room
import com.mielechm.pixbaycodechallenge.data.ImagesDao
import com.mielechm.pixbaycodechallenge.data.ImagesDatabase
import com.mielechm.pixbaycodechallenge.data.remote.PixbayApi
import com.mielechm.pixbaycodechallenge.repositories.DefaultImagesRepository
import com.mielechm.pixbaycodechallenge.repositories.ImagesRepository
import com.mielechm.pixbaycodechallenge.utils.API_KEY
import com.mielechm.pixbaycodechallenge.utils.BASE_URL
import com.mielechm.pixbaycodechallenge.utils.DefaultDispatchers
import com.mielechm.pixbaycodechallenge.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePixbayApi(): PixbayApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val url = chain.request().url.newBuilder().addQueryParameter("key", API_KEY).build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }.addInterceptor(loggingInterceptor).build()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
            .create()

    }

    @Singleton
    @Provides
    fun provideImagesDb(@ApplicationContext context: Context): ImagesDatabase {
        return Room.databaseBuilder(
            context,
            ImagesDatabase::class.java,
            ImagesDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideImagesDao(imagesDatabase: ImagesDatabase): ImagesDao = imagesDatabase.imagesDao()

    @Singleton
    @Provides
    fun provideDefaultImagesRepository(api: PixbayApi, dao: ImagesDao) =
        DefaultImagesRepository(api, dao) as ImagesRepository

    @Singleton
    @Provides
    fun provideDefaultDispatcher() = DefaultDispatchers() as DispatcherProvider
}
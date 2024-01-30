package com.mielechm.pixbaycodechallenge.di

import com.mielechm.pixbaycodechallenge.data.remote.PixbayApi
import com.mielechm.pixbaycodechallenge.repositories.DefaultImagesRepository
import com.mielechm.pixbaycodechallenge.utils.API_KEY
import com.mielechm.pixbaycodechallenge.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideDefaultImagesRepository(api: PixbayApi) = DefaultImagesRepository(api)

}
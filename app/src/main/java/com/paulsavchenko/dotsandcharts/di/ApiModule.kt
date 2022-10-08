package com.paulsavchenko.dotsandcharts.di

import com.paulsavchenko.dotsandcharts.data.remote.API
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideApi(moshi: Moshi): API = Retrofit
        .Builder()
        .baseUrl(API.API_ROOT)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(API::class.java)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}
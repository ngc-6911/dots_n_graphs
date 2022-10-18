package com.paulsavchenko.dotsandcharts.di

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SystemModule {

    @Singleton
    @Provides
    fun provideContentResolver(context: Context): ContentResolver = context.contentResolver
//
//    @Singleton
//    @Provides
//    fun provideContext(context: Context): Context = context
}
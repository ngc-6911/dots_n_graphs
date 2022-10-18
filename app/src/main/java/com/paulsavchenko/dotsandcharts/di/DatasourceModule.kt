package com.paulsavchenko.dotsandcharts.di

import android.content.ContentResolver
import android.content.Context
import com.paulsavchenko.dotsandcharts.data.DotsDatasource
import com.paulsavchenko.dotsandcharts.data.ImagesDatasource
import com.paulsavchenko.dotsandcharts.data.PermissionsDatasource
import com.paulsavchenko.dotsandcharts.data.remote.API
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class, SystemModule::class, ])
object DatasourceModule {

    @Singleton
    @Provides
    fun providesDotsDatasourceModule(api: API): DotsDatasource = DotsDatasource.Impl(api)

    @Singleton
    @Provides
    fun providesImagesDatasource(
        contentResolver: ContentResolver
    ): ImagesDatasource = ImagesDatasource.Impl(contentResolver)


    @Singleton
    @Provides
    fun providesPermissionsDatasource(
        context: Context,
    ): PermissionsDatasource = PermissionsDatasource.Impl(context)
}
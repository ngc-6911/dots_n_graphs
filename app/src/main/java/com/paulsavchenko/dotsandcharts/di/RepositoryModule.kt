package com.paulsavchenko.dotsandcharts.di

import com.paulsavchenko.dotsandcharts.data.DotsDatasource
import com.paulsavchenko.dotsandcharts.data.ImagesDatasource
import com.paulsavchenko.dotsandcharts.data.PermissionsDatasource
import com.paulsavchenko.dotsandcharts.domain.DotsRepository
import com.paulsavchenko.dotsandcharts.domain.ImagesRepository
import com.paulsavchenko.dotsandcharts.domain.PermissionsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [DatasourceModule::class, ])
object RepositoryModule {

    @Singleton
    @Provides
    fun providesDotsRepository(dotsDatasource: DotsDatasource): DotsRepository = DotsRepository.Impl(dotsDatasource)

    @Singleton
    @Provides
    fun providesImagesRepository(
        imagesDatasource: ImagesDatasource,
    ): ImagesRepository = ImagesRepository.Impl(imagesDatasource)

    @Singleton
    @Provides
    fun providesPermissionsRepository(
        permissionsDatasource: PermissionsDatasource,
    ): PermissionsRepository = PermissionsRepository.Impl(permissionsDatasource)
}
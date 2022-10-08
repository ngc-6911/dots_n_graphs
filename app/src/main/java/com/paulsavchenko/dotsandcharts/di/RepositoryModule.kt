package com.paulsavchenko.dotsandcharts.di

import com.paulsavchenko.dotsandcharts.data.DotsDatasource
import com.paulsavchenko.dotsandcharts.domain.DotsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [DatasourceModule::class, ])
object RepositoryModule {

    @Singleton
    @Provides
    fun providesDotsRepository(dotsDatasource: DotsDatasource): DotsRepository = DotsRepository.Impl(dotsDatasource)

}
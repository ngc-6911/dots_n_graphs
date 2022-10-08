package com.paulsavchenko.dotsandcharts.di

import com.paulsavchenko.dotsandcharts.data.DotsDatasource
import com.paulsavchenko.dotsandcharts.data.remote.API
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class, ])
object DatasourceModule {

    @Singleton
    @Provides
    fun providesDotsDatasourceModule(api: API): DotsDatasource = DotsDatasource.Impl(api)
}
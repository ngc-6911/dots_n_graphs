package com.paulsavchenko.dotsandcharts.di

import com.paulsavchenko.dotsandcharts.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ RepositoryModule::class, ])
abstract class MainComponent {


    @Component.Factory
    @Suppress("unused")
    interface Factory {
        fun create(): MainComponent
    }

    abstract val mainViewModel: MainViewModel
}
package com.paulsavchenko.dotsandcharts.di

import android.content.Context
import com.paulsavchenko.dotsandcharts.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ RepositoryModule::class, ])
abstract class MainComponent {


    @Component.Factory
    @Suppress("unused")
    interface Factory {
        fun create(@BindsInstance context: Context): MainComponent
    }

    abstract val mainViewModel: MainViewModel
}
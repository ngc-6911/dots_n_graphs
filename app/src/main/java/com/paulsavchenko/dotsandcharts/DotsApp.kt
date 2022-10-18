package com.paulsavchenko.dotsandcharts

import android.app.Application
import com.paulsavchenko.dotsandcharts.di.DaggerMainComponent
import com.paulsavchenko.dotsandcharts.di.MainComponent

class DotsApp: Application() {

    private lateinit var _dagger: MainComponent

    val dagger: MainComponent get() = _dagger

    override fun onCreate() {
        super.onCreate()
        _dagger = DaggerMainComponent.factory().create(applicationContext)
    }
}
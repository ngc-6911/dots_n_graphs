package com.paulsavchenko.dotsandcharts

import android.app.Application
import com.paulsavchenko.dotsandcharts.di.DaggerMainComponent
import com.paulsavchenko.dotsandcharts.di.MainComponent

class DotsApp: Application() {
    val dagger: MainComponent = DaggerMainComponent.create()
}
package com.paulsavchenko.dotsandcharts

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> ComponentActivity.vmDelegate(crossinline factory: () -> T): Lazy<T> {
    return viewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
        }
    }
}
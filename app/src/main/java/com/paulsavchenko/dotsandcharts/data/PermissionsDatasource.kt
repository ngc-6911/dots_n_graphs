package com.paulsavchenko.dotsandcharts.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

interface PermissionsDatasource {

    fun checkStoragePermissions(): Boolean

    class Impl(
        private val context: Context,
    ): PermissionsDatasource {
        override fun checkStoragePermissions(): Boolean {
            return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ) == PackageManager.PERMISSION_GRANTED
        }

    }
}
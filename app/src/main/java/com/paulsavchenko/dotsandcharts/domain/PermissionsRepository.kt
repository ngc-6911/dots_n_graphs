package com.paulsavchenko.dotsandcharts.domain

import com.paulsavchenko.dotsandcharts.data.PermissionsDatasource

interface PermissionsRepository {

    fun checkStoragePermissions(): Boolean

    class Impl(
        private val permissionsDatasource: PermissionsDatasource,
    ): PermissionsRepository {
        override fun checkStoragePermissions(): Boolean {
            return permissionsDatasource.checkStoragePermissions()
        }

    }
}
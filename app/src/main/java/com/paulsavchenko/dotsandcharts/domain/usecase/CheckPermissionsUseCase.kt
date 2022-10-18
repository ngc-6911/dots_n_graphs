package com.paulsavchenko.dotsandcharts.domain.usecase

import com.paulsavchenko.dotsandcharts.domain.PermissionsRepository
import com.paulsavchenko.dotsandcharts.domain.base.SimpleUseCase
import javax.inject.Inject

class CheckStoragePermissionsUseCase @Inject constructor(
    private val permissionsRepository: PermissionsRepository,
): SimpleUseCase<Boolean>() {

    override suspend fun execute(): Boolean {
        return permissionsRepository.checkStoragePermissions()
    }
}
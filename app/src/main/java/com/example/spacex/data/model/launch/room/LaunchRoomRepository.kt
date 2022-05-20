package com.example.spacex.data.model.launch.room

import com.example.spacex.data.model.launch.LaunchRepository
import com.example.spacex.ui.main.launch.view.LaunchModel

class LaunchRoomRepository(private val launchDao: LaunchDao) : LaunchRepository {
    override suspend fun getFavoriteLaunches(): List<LaunchEntity> = launchDao.getFavorites()

    override suspend fun setLaunchFavorite(launchModel: LaunchModel) =
        launchDao.setLaunchIsFavorite(LaunchEntity(launchModel.launch.id, launchModel.isFavorite))
}
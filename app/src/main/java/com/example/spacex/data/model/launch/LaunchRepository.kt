package com.example.spacex.data.model.launch

import com.example.spacex.data.model.launch.room.LaunchEntity
import com.example.spacex.ui.main.launch.view.LaunchModel

interface LaunchRepository {
    suspend fun getFavoriteLaunches(): List<LaunchEntity>
    suspend fun setLaunchFavorite(launchModel: LaunchModel)
}
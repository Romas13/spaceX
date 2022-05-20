package com.example.spacex.data.repository

import android.content.Context
import androidx.room.Room
import com.example.spacex.data.api.SpaceXApi
import com.example.spacex.data.api.SpaceXService
import com.example.spacex.data.model.launch.LaunchRepository
import com.example.spacex.data.model.launch.room.LaunchRoomRepository
import com.example.spacex.data.repository.room.AppDataBase

object Repositories {

    private lateinit var applicationContext: Context

    private val database: AppDataBase by lazy {
        Room.databaseBuilder(applicationContext, AppDataBase::class.java, "database.db")
            .build()
    }

    val spaceXApi: SpaceXApi by lazy {
        SpaceXService.getInstance()
    }

    val launchesRepository: LaunchRepository by lazy {
        LaunchRoomRepository(database.getLaunchDao())
    }

    fun init(context: Context) {
        applicationContext = context
    }
}
package com.example.spacex.data.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spacex.data.model.launch.room.LaunchDao
import com.example.spacex.data.model.launch.room.LaunchEntity

@Database(
    version = 1,
    entities = [LaunchEntity::class]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getLaunchDao(): LaunchDao
}
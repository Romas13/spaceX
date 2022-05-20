package com.example.spacex.data.model.launch.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LaunchDao {
    @Query("SELECT * FROM launches")
    fun getFavorites(): List<LaunchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setLaunchIsFavorite(launch: LaunchEntity)

}
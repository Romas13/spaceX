package com.example.spacex.data.model.launch.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spacex.data.model.launch.entities.Launch

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)
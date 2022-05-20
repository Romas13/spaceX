package com.example.spacex

import android.app.Application
import com.example.spacex.data.repository.Repositories

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Repositories.init(this)
    }
}
package com.thisisnotyours.registertaxikotlin.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
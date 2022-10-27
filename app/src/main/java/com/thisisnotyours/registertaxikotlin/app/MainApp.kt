package com.thisisnotyours.registertaxikotlin.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


//open class - 부모 클래스
@HiltAndroidApp
open class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
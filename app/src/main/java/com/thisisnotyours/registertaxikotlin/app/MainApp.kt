package com.thisisnotyours.registertaxikotlin.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


//open class - 부모 클래스
@HiltAndroidApp
open class MainApp : Application() {

//    companion object {
//        lateinit var appContext: Context
//    }

    override fun onCreate() {
        super.onCreate()
//        appContext = applicationContext
    }
}
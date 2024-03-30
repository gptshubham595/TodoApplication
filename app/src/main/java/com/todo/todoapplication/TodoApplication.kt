package com.todo.todoapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
//            Log.d("TodoApplication", "onCreate ${BuildConfig.API_KEY}")
        }
    }
}

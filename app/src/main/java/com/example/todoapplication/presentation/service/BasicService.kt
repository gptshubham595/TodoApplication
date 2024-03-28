package com.example.todoapplication.presentation.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

open class BasicService : Service() {
    private val binder = BasicBinder()

    inner class BasicBinder : Binder() {
        fun getService(): BasicService {
            return this@BasicService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val TAG = "BasicService"
    }
}

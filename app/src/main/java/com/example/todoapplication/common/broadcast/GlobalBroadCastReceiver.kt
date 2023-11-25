package com.example.todoapplication.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todoapplication.common.Constant.Companion.EXTRA_GLOBAL_TODO_ACTION
import com.example.todoapplication.common.Constant.Companion.GLOBAL_TODO_ACTION

class GlobalBroadCastReceiver(private val listener: GlobalBroadCastListener): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when(it.action){
                GLOBAL_TODO_ACTION -> {
                    listener.onGlobalBroadCastListener(it.getStringExtra(EXTRA_GLOBAL_TODO_ACTION))
                }
            }
        }
    }
}

interface GlobalBroadCastListener {
    fun onGlobalBroadCastListener(message: String?)
}
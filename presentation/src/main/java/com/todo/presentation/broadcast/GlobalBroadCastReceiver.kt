package com.todo.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.todo.common.EXTRA_GLOBAL_TODO_ACTION
import com.todo.common.GLOBAL_TODO_ACTION
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalBroadCastReceiver @Inject constructor() : BroadcastReceiver() {
    private lateinit var listener: GlobalBroadCastListener
    fun setListener(listener: GlobalBroadCastListener) {
        this.listener = listener
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                GLOBAL_TODO_ACTION -> {
                    if (::listener.isInitialized) {
                        listener.onGlobalBroadCastListener(it.getStringExtra(EXTRA_GLOBAL_TODO_ACTION))
                    }
                }
            }
        }
    }
}

interface GlobalBroadCastListener {
    fun onGlobalBroadCastListener(message: String?)
}

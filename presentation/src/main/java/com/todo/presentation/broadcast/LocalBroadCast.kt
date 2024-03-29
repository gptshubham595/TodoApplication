package com.todo.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.todo.common.Constant.Companion.EXTRA_LOCAL_TODO_ACTION
import com.todo.common.Constant.Companion.LOCAL_TODO_ACTION

class LocalBroadCast(private val listener: TodoBroadCastListener) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                LOCAL_TODO_ACTION -> {
                    listener.onLocalTodoReceived(it.getStringExtra(EXTRA_LOCAL_TODO_ACTION))
                }
            }
        }
    }
}

interface TodoBroadCastListener {
    fun onLocalTodoReceived(message: String?)
}
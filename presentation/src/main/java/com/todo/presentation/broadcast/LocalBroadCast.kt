package com.todo.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.todo.common.EXTRA_LOCAL_TODO_ACTION
import com.todo.common.LOCAL_TODO_ACTION
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalBroadCast @Inject constructor(private val listener: TodoBroadCastListener) : BroadcastReceiver() {
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

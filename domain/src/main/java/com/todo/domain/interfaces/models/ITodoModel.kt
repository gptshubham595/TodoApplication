package com.todo.domain.interfaces.models

import com.todo.common.Utils

interface ITodoItem {
    val id: Long
    val task: String
    val status: Utils.TodoStatus
}

package com.todo.domain.models

import com.todo.common.Utils

data class TodoItem(
    val id: Long,
    val task: String,
    val status: Utils.TodoStatus
)

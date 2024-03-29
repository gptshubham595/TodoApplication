package com.todo.domain.interfaces.models

import com.todo.common.Utils

interface ITodoItem {
    val id: Long
    val task: String
    val status: Utils.TodoStatus
}

data class TodoItem(
    override val id: Long,
    override val task: String,
    override val status: Utils.TodoStatus
) : ITodoItem

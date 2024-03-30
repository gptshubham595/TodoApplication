package com.todo.domain.models

data class TodoItem(
    val id: Long,
    val task: String,
    val status: String
)

package com.example.todoapplication.domain.interfaces.models

import com.example.todoapplication.common.Utils

interface ITodoItem {
    val id: Long
    val task: String
    val status: Utils.TodoStatus
}


package com.example.todoapplication.data.database.interfaces

import com.example.todoapplication.data.models.TodoItem

interface ITodoDB {
    suspend fun fetchAllTodoItems(): List<TodoItem>
    suspend fun addTodoItem(todoItem: TodoItem): Long

    suspend fun deleteTodoItem(todoId: Long): Int

    suspend fun updateTodoItem(todoItem: TodoItem): Int
}

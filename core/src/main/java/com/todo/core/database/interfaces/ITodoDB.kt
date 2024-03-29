package com.todo.core.database.interfaces

import com.todo.data.models.TodoItem

interface ITodoDB {
    suspend fun fetchAllTodoItems(): List<TodoItem>
    suspend fun addTodoItem(todoItem: TodoItem): Long

    suspend fun deleteTodoItem(todoId: Long): Int

    suspend fun updateTodoItem(todoItem: TodoItem): Int
}

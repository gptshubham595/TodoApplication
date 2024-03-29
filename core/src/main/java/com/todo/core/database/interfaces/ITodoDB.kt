package com.todo.core.database.interfaces

import com.todo.data.models.TodoItemEntity

interface ITodoDB {
    suspend fun fetchAllTodoItems(): List<TodoItemEntity>
    suspend fun addTodoItem(todoItemEntity: TodoItemEntity): Long

    suspend fun deleteTodoItem(todoId: Long): Int

    suspend fun updateTodoItem(todoItemEntity: TodoItemEntity): Int
}

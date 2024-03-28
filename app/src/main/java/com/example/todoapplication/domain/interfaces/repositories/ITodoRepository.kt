package com.example.todoapplication.domain.interfaces.repositories

import com.example.todoapplication.common.Utils.Either
import com.example.todoapplication.data.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface ITodoRepository {

    suspend fun getTodoList(): Either<Exception, Flow<List<TodoItem>>>

    suspend fun addTodoItem(todoItem: TodoItem): Either<Exception, Flow<Long>>

    suspend fun deleteTodoItem(todoId: Long): Either<Exception, Flow<Int>>

    suspend fun updateTodoItem(todoItem: TodoItem): Either<Exception, Flow<Int>>
}

package com.todo.domain.interfaces.repositories

import com.todo.common.Utils.Either
import com.todo.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun getTodoList(): Either<Exception, Flow<List<TodoItem>>>

    suspend fun addTodoItem(todoItem: TodoItem): Either<Exception, Flow<Long>>

    suspend fun deleteTodoItem(todoId: Long): Either<Exception, Flow<Int>>

    suspend fun updateTodoItem(todoItem: TodoItem): Either<Exception, Flow<Int>>
}

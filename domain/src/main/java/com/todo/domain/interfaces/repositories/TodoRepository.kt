package com.todo.domain.interfaces.repositories

import com.todo.common.Utils.Either
import com.todo.domain.interfaces.models.ITodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun getTodoList(): Either<Exception, Flow<List<ITodoItem>>>

    suspend fun addTodoItem(todoItem: ITodoItem): Either<Exception, Flow<Long>>

    suspend fun deleteTodoItem(todoId: Long): Either<Exception, Flow<Int>>

    suspend fun updateTodoItem(todoItem: ITodoItem): Either<Exception, Flow<Int>>
}

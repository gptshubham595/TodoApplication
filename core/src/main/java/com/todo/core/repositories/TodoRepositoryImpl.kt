package com.todo.core.repositories

import com.todo.common.Utils.Either
import com.todo.core.database.api.ApiInterface
import com.todo.core.database.interfaces.ITodoDB
import com.todo.core.di.qualifier.ProcessorRoomDB
import com.todo.data.models.TodoItemEntity
import com.todo.domain.interfaces.models.ITodoItem
import com.todo.domain.interfaces.repositories.TodoRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TodoRepositoryImpl @Inject constructor(
    @ProcessorRoomDB private val todoDao: ITodoDB, // IDatabase
    private val apiInterface: ApiInterface // IApi
) : TodoRepository {
    override suspend fun getTodoList(): Either<Exception, Flow<List<TodoItemEntity>>> {
        return try {
            Either.Success(
                flow {
                    emit(todoDao.fetchAllTodoItems())
                    emit(apiInterface.getPosts())
                }.flowOn(Dispatchers.IO)
            )
        } catch (e: Exception) {
            Either.Error(e)
        }
    }

    override suspend fun addTodoItem(todoItem: ITodoItem): Either<Exception, Flow<Long>> {
        return try {
            Either.Success(flow { emit(todoDao.addTodoItem(todoItem as TodoItemEntity)) }.flowOn(Dispatchers.IO))
        } catch (e: Exception) {
            Either.Error(e)
        }
    }

    override suspend fun deleteTodoItem(todoId: Long): Either<Exception, Flow<Int>> {
        return try {
            Either.Success(flow { emit(todoDao.deleteTodoItem(todoId)) }.flowOn(Dispatchers.IO))
        } catch (e: Exception) {
            Either.Error(e)
        }
    }

    override suspend fun updateTodoItem(todoItem: ITodoItem): Either<Exception, Flow<Int>> {
        return try {
            Either.Success(flow { emit(todoDao.updateTodoItem(todoItem as TodoItemEntity)) }.flowOn(Dispatchers.IO))
        } catch (e: Exception) {
            Either.Error(e)
        }
    }
}

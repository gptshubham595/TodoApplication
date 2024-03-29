package com.todo.core.repositories

import com.todo.common.Utils.Either
import com.todo.core.database.api.ApiInterface
import com.todo.core.database.interfaces.TodoDataSource
import com.todo.core.di.qualifier.RoomDatabaseQualifier
import com.todo.core.transformer.toData
import com.todo.core.transformer.toDomain
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.models.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    @RoomDatabaseQualifier private val todoDao: TodoDataSource, // IDatabase
    private val apiInterface: ApiInterface // IApi
) : TodoRepository {
    override suspend fun getTodoList(): Either<Exception, Flow<List<TodoItem>>> {
        return try {
            Either.Success(
                flow {
                    emit(todoDao.fetchAllTodoItems().map { it.toDomain() })
                    emit(apiInterface.getPosts().map { it.toDomain() })
                }.flowOn(Dispatchers.IO)
            )
        } catch (e: Exception) {
            Either.Error(e)
        }
    }

    override suspend fun addTodoItem(todoItem: TodoItem): Either<Exception, Flow<Long>> {
        return try {
            Either.Success(flow { emit(todoDao.addTodoItem(todoItem.toData())) }.flowOn(Dispatchers.IO))
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

    override suspend fun updateTodoItem(todoItem: TodoItem): Either<Exception, Flow<Int>> {
        return try {
            Either.Success(flow { emit(todoDao.updateTodoItem(todoItem.toData())) }.flowOn(Dispatchers.IO))
        } catch (e: Exception) {
            Either.Error(e)
        }
    }
}

package com.example.todoapplication.data.repositories

import com.example.todoapplication.common.Utils.Either
import com.example.todoapplication.data.database.api.ApiInterface
import com.example.todoapplication.data.database.cached.TodoDao
import com.example.todoapplication.data.database.interfaces.ITodoDB
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.di.qualifier.ProcessorRoomDB
import com.example.todoapplication.domain.interfaces.repositories.ITodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    @ProcessorRoomDB private val todoDao: ITodoDB, // IDatabase
    private val apiInterface: ApiInterface //IApi
) : ITodoRepository {
    override suspend fun getTodoList(): Either<Exception, Flow<List<TodoItem>>> {
        return try {
            Either.Success(flow {

                emit(todoDao.fetchAllTodoItems())
                emit(apiInterface.getPosts())

            }.flowOn(Dispatchers.IO))
        } catch (e: Exception) {
            Either.Error(e)
        }
    }

    override suspend fun addTodoItem(todoItem: TodoItem): Either<Exception, Flow<Long>> {
        return try {
            Either.Success(flow { emit(todoDao.addTodoItem(todoItem)) }.flowOn(Dispatchers.IO))
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
            Either.Success(flow { emit(todoDao.updateTodoItem(todoItem)) }.flowOn(Dispatchers.IO))
        } catch (e: Exception) {
            Either.Error(e)
        }
    }
}
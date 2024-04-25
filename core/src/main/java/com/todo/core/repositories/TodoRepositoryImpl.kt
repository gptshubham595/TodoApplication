package com.todo.core.repositories

import android.util.Log
import com.todo.common.Utils.Either
import com.todo.core.di.qualifier.RoomDatabaseQualifier
import com.todo.core.network.api.ApiService
import com.todo.core.transformer.toData
import com.todo.core.transformer.toDomain
import com.todo.data.interfaces.TodoDataSource
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.models.TodoItem
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class TodoRepositoryImpl @Inject constructor(
    @RoomDatabaseQualifier private val todoDao: TodoDataSource, // IDatabase
    private val apiService: ApiService // IApi
) : TodoRepository {
    override suspend fun getTodoList(): Either<Exception, Flow<List<TodoItem>>> {
        return try {
            val dataFlow = flow {
                Log.d("ResponseDB", "${todoDao.fetchAllTodoItems().map { it.toDomain() }}")
                emit(todoDao.fetchAllTodoItems().map { it.toDomain() })
            }.catch { e ->
            }.flowOn(Dispatchers.IO).onCompletion {
                Log.d("ResponseDB", "onCompletion")
            }

            val apiFlow = flow {
                val apiResponse: List<TodoItem> = apiService.getPosts().map { it.toDomain() }
                Log.d("ResponseAPI", "$apiResponse")

                apiResponse.let {
                    saveToDB(apiResponse)
                    emit(apiResponse)
                }
            }.flowOn(Dispatchers.IO)

            Either.Success(dataFlow.combine(apiFlow) { data, apiData -> data + apiData })
        } catch (e: Exception) {
            Either.Error(e)
        }
    }

    private suspend fun saveToDB(apiResponse: List<TodoItem>) {
        apiResponse.forEach {
            todoDao.addTodoItem(it.toData())
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

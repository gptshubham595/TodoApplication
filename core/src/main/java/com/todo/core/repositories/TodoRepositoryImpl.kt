package com.todo.core.repositories

import com.todo.common.Utils.Either
import com.todo.core.database.api.ApiInterface
import com.todo.core.database.interfaces.TodoDataSource
import com.todo.core.di.qualifier.SharedPrefDatabaseQualifier
import com.todo.core.transformer.toData
import com.todo.core.transformer.toDomain
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.models.TodoItem
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TodoRepositoryImpl @Inject constructor(
    @SharedPrefDatabaseQualifier private val todoDao: TodoDataSource, // IDatabase
    private val apiInterface: ApiInterface // IApi
) : TodoRepository {
    override suspend fun getTodoList(): Either<Exception, Flow<List<TodoItem>>> {
        try {
            var f1: Flow<List<TodoItem>>? = null
            var f2: Flow<List<TodoItem>>? = null

            val data = todoDao.fetchAllTodoItems()
            data?.let {
                f1 = flow {
                    emit(it.map { it.toDomain() })
                }.flowOn(Dispatchers.IO)
            }

            val api = apiInterface.getPosts()

            api?.let {
                f2 = flow {
                    emit(it.map { it.toDomain() })
                }.flowOn(Dispatchers.IO)
            }
            if (f1 != null && f2 != null) {
                return Either.Success(f1!!.combine(f2!!) { a, b -> a + b })
            } else if (f1 != null) {
                return Either.Success(f1!!)
            } else if (f2 != null) {
                return Either.Success(f2!!)
            } else {
                return Either.Error(Exception("No data found"))
            }
        } catch (e: Exception) {
            return Either.Error(e)
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

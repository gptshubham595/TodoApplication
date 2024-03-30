package com.todo.core.repositories

import com.todo.common.Utils.Either
import com.todo.core.database.api.ApiInterface
import com.todo.core.database.interfaces.TodoDataSource
import com.todo.core.di.qualifier.RealmDBQualifier
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
    @RealmDBQualifier private val todoDao: TodoDataSource, // IDatabase
    private val apiInterface: ApiInterface // IApi
) : TodoRepository {
    override suspend fun getTodoList(): Either<Exception, Flow<List<TodoItem>>> {
        try {
            var dataFlow: Flow<List<TodoItem>>? = null
            var apiFlow: Flow<List<TodoItem>>? = null

            val data = todoDao.fetchAllTodoItems()
            data?.let {
                dataFlow = flow {
                    emit(it.map { it.toDomain() })
                }.flowOn(Dispatchers.IO)
            }

            val api = apiInterface.getPosts()

            api?.let {
                apiFlow = flow {
                    emit(it.map { it.toDomain() })
                }.flowOn(Dispatchers.IO)
            }
            if (dataFlow != null && apiFlow != null) {
                return Either.Success(dataFlow!!.combine(apiFlow!!) { a, b -> a + b })
            } else if (dataFlow != null) {
                return Either.Success(dataFlow!!)
            } else if (apiFlow != null) {
                return Either.Success(apiFlow!!)
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

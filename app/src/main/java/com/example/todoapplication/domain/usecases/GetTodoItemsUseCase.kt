package com.example.todoapplication.domain.usecases

import android.util.Log
import com.example.todoapplication.common.Utils.Either
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.domain.interfaces.repositories.ITodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTodoItemsUseCase @Inject constructor(
    private val todoRepositoryImpl: ITodoRepository
) : BaseUseCase<Unit, Flow<List<TodoItem>>>() {
    override suspend fun run(params: Unit): Either<Exception, Flow<List<TodoItem>>> {
        Log.d("GetTodoItemsUseCase", "run: ${System.identityHashCode(this)}")
        return todoRepositoryImpl.getTodoList()
    }
}
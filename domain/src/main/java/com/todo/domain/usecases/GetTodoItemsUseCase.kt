package com.todo.domain.usecases

import android.util.Log
import com.todo.common.Utils.Either
import com.todo.domain.interfaces.models.ITodoItem
import com.todo.domain.interfaces.repositories.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTodoItemsUseCase @Inject constructor(
    private val todoRepositoryImpl: TodoRepository
) : BaseUseCase<Unit, Flow<List<ITodoItem>>>() {
    override suspend fun run(params: Unit): Either<Exception, Flow<List<ITodoItem>>> {
        Log.d("GetTodoItemsUseCase", "run: ${System.identityHashCode(this)}")
        return todoRepositoryImpl.getTodoList()
    }
}

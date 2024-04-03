package com.todo.domain.usecases

import android.util.Log
import com.todo.common.Utils.Either
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.models.TodoItem
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class UpdateTodoItemsUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : BaseUseCase<TodoItem, Flow<Int>>() {
    override suspend fun run(params: TodoItem): Either<Exception, Flow<Int>> {
        Log.d("GetTodoItemsUseCase", "run: ${System.identityHashCode(this)}")
        return todoRepository.updateTodoItem(params)
    }
}

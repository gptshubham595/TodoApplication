package com.todo.domain.usecases

import android.util.Log
import com.todo.common.Utils.Either
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.models.TodoItem
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class GetTodoItemsUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : BaseUseCase<Unit, Flow<List<TodoItem>>>() {
    override suspend fun run(params: Unit): Either<Exception, Flow<List<TodoItem>>> {
        Log.d("GetTodoItemsUseCase", "run: ${System.identityHashCode(this)}")
        return todoRepository.getTodoList()
    }
}

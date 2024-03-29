package com.todo.domain.usecases

import com.todo.common.Utils
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.models.TodoItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AddTodoItemUseCase @Inject constructor(
    private val todoRepositoryImpl: TodoRepository
) : BaseUseCase<TodoItem, Flow<Long>>() {
    override suspend fun run(params: TodoItem): Utils.Either<Exception, Flow<Long>> {
        return todoRepositoryImpl.addTodoItem(params)
    }
}

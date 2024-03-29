package com.todo.domain.usecases

import com.todo.common.Utils
import com.todo.domain.interfaces.models.ITodoItem
import com.todo.domain.interfaces.repositories.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddTodoItemUseCase @Inject constructor(
    private val todoRepositoryImpl: TodoRepository
) : BaseUseCase<ITodoItem, Flow<Long>>() {
    override suspend fun run(params: ITodoItem): Utils.Either<Exception, Flow<Long>> {
        return todoRepositoryImpl.addTodoItem(params)
    }
}

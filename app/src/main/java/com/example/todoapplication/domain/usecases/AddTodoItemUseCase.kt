package com.example.todoapplication.domain.usecases

import com.example.todoapplication.common.Utils
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.domain.interfaces.repositories.ITodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AddTodoItemUseCase @Inject constructor(
    private val todoRepositoryImpl: ITodoRepository
) : BaseUseCase<TodoItem, Flow<Long>>() {
    override suspend fun run(params: TodoItem): Utils.Either<Exception, Flow<Long>> {
        return todoRepositoryImpl.addTodoItem(params)
    }
}
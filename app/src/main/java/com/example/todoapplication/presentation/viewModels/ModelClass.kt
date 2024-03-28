package com.example.todoapplication.presentation.viewModels

import com.example.todoapplication.domain.usecases.GetTodoItemsUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ModelClass @Inject constructor(
    private val getTodoItemsUseCase: GetTodoItemsUseCase
) {
    val scope = CoroutineScope(Dispatchers.IO + Job())
    fun getTodoItems() {
        getTodoItemsUseCase(
            scope = scope,
            params = Unit,
            onSuccess = {
                scope.launch {
                    it.collect {
                    }
                }
            },
            onFailure = {
            }
        )
    }
}

package com.todo.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.domain.models.TodoItem
import com.todo.domain.usecases.AddTodoItemUseCase
import com.todo.domain.usecases.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class TodoViewModel2 @Inject constructor(
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase
) : ViewModel() {

    private val _todoItemsListLiveData = MutableLiveData<List<TodoItem>>()
    val todoItemsListLiveData = _todoItemsListLiveData as LiveData<List<TodoItem>>

    init {
        Log.d("viewModel2", "${System.identityHashCode(getTodoItemsUseCase)}")
    }

    fun getTodoItems() {
        viewModelScope.launch {
            getTodoItemsUseCase(
                scope = this,
                params = Unit,
                onSuccess = {
                    this.launch {
                        it.collect {
                            Log.d("TodoViewModel", "collected getTodoItems: $it")
                            _todoItemsListLiveData.postValue(it)
                            EventBus.getDefault().post(TodoViewModel.Companion.DataEvent("hi"))
                        }
                    }
                },
                onFailure = {
                }
            )
        }
    }
}

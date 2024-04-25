package com.todo.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.domain.models.TodoItem
import com.todo.domain.usecases.AddTodoItemUseCase
import com.todo.domain.usecases.GetTodoItemsUseCase
import com.todo.domain.usecases.UpdateTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val updateTodoItemsUseCase: UpdateTodoItemsUseCase
) : ViewModel() {

    private val _todoItemsListLiveData = MutableLiveData<List<TodoItem>>()
    val todoItemsListLiveData = _todoItemsListLiveData as LiveData<List<TodoItem>>

    private val mutableStateFlow = MutableStateFlow(0)
    val stateFlow: StateFlow<Int> = mutableStateFlow

    val mutableSharedFlow = MutableSharedFlow<Int>()
    val sharedFlow: SharedFlow<Int> = mutableSharedFlow

    init {
        Log.d("viewModel1", "${System.identityHashCode(getTodoItemsUseCase)}")
    }

    fun getTodoItems() {
        viewModelScope.launch {
            getTodoItemsUseCase(
                scope = this,
                params = Unit,
                onSuccess = {
                    this.launch {
                        it.collect {
                            Log.d("TodoViewModel", "collector 1 $it")
                            Log.d("TodoViewModel", "collected getTodoItems: $it")
                            if ((it.size ?: 0) > 0L) {
                                _todoItemsListLiveData.postValue(it)
                            }
                            EventBus.getDefault().post(DataEvent("hi"))
                        }
                    }
                },
                onFailure = {
                }
            )

// Update the state
            launch {
                repeat(3) { i ->
                    delay(1000)
                    mutableStateFlow.value = i
                    mutableSharedFlow.emit(i)
                }
            }
        }
    }

    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            addTodoItemUseCase(
                scope = this,
                params = todoItem,
                onSuccess = {
                    this.launch {
                        it.collect {
                            Log.d("TodoViewModel", "collected addTodoItem: $it")
                        }
                    }
                },
                onFailure = {
                }
            )
        }
    }

    fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            updateTodoItemsUseCase(
                scope = this,
                params = todoItem,
                onSuccess = {
                    this.launch {
                        it.collect {
                            Log.d("TodoViewModel", "collected updateTodoItem: $it")
                        }
                    }
                },
                onFailure = {
                }
            )
        }
    }

    companion object {
        const val TAG = "TodoViewModel"

        class DataEvent(val data: String)
    }
}

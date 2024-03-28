package com.example.todoapplication.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.domain.usecases.AddTodoItemUseCase
import com.example.todoapplication.domain.usecases.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class DataEvent(val data: String)

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase
) : ViewModel() {

    private val _todoItemsListLiveData = MutableLiveData<List<TodoItem>>()
    val todoItemsListLiveData = _todoItemsListLiveData as LiveData<List<TodoItem>>

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
                            Log.d("TodoViewModel", "collected getTodoItems: $it")
                            _todoItemsListLiveData.postValue(it)
                            EventBus.getDefault().post(DataEvent("hi"))
                        }
                    }
                },
                onFailure = {
                }
            )
        }
    }
}

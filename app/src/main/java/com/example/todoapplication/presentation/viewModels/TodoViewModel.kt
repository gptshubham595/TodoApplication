package com.example.todoapplication.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.domain.usecases.AddTodoItemUseCase
import com.example.todoapplication.domain.usecases.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class DataEvent(val data: String)

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase
) : ViewModel() {

    val viewModelScope = CoroutineScope(Dispatchers.IO) + Job()


    private val _todoItemsListLiveData = MutableLiveData<List<TodoItem>>()
    val todoItemsListLiveData = _todoItemsListLiveData as LiveData<TodoItem>

    fun getTodoItems() {
        viewModelScope.launch {
            getTodoItemsUseCase(
                scope = this,
                params = Unit,
                onSuccess = {
                    this.launch {
                        it.collect {
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
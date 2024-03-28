package com.example.todoapplication.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todoapplication.TodoApplication
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.domain.usecases.AddTodoItemUseCase
import com.example.todoapplication.domain.usecases.GetTodoItemsUseCase
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
        Log.d("viewModel2","${System.identityHashCode(getTodoItemsUseCase)}")
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

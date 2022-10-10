package com.namasake.task.feature_task.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namasake.task.feature_task.core.util.Resource
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.doman.use_case.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val useCase: UseCase): ViewModel() {
    sealed class TaskEvent {
        class Success(val result: List<Task>?): TaskEvent()
        class Failure(val error: String): TaskEvent()
        object Loading : TaskEvent()
        object Empty : TaskEvent()
    }

    var saveResponse: MutableLiveData<Response<Task>> = MutableLiveData()
    var updateResponse:MutableLiveData<Response<Task>> = MutableLiveData()

    private val _task = MutableStateFlow<TaskEvent>(TaskEvent.Empty)
    val task: StateFlow<TaskEvent> = _task


    fun getNewTask(){
        viewModelScope.launch (Dispatchers.IO){
            useCase.fetchTasks().collect{ result ->
                when(result){
                    is Resource.Success ->
                    {
                        val response = result.data
                        if (response == null){
                            _task.value = TaskEvent.Failure("Unexpected Error")
                        } else{
                            _task.value = TaskEvent.Success(response)
                        }
                    }
                    is Resource.Loading ->
                    {
                        _task.value = TaskEvent.Loading
                    }
                    is Resource.Error -> _task.value = TaskEvent.Failure(result.message.toString())
                }
            }
        }
    }

    fun saveNewTask(task: Task){
        viewModelScope.launch {
            val response = useCase.saveNewTask(task)
            saveResponse.value = response
        }
    }
    suspend fun deleteTask(id: Int){
        useCase.deleteTask(id)
    }

    suspend fun updateTask(id: Int,completed:Boolean){
        viewModelScope.launch {
            val res = useCase.updateTask(id,completed)
            updateResponse.value = res
        }
    }

}
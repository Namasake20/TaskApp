package com.namasake.task.feature_task.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namasake.task.feature_task.core.util.Resource
import com.namasake.task.feature_task.data.remote.TaskDto
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.doman.use_case.GetTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val getTasks: GetTasks): ViewModel() {
    sealed class TaskEvent {
        class Success(val result: List<Task>?): TaskEvent()
        class Failure(val error: String): TaskEvent()
        object Loading : TaskEvent()
        object Empty : TaskEvent()
    }

    private val _task = MutableStateFlow<TaskEvent>(TaskEvent.Empty)
    val task: StateFlow<TaskEvent> = _task

    fun getNewTask(){
        viewModelScope.launch (Dispatchers.IO){
            getTasks().collect{result ->
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


}
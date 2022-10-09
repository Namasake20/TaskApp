package com.namasake.task.feature_task.doman.repo

import com.namasake.task.feature_task.core.util.Resource
import com.namasake.task.feature_task.doman.model.Task
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MainRepo {
    fun getTasks(): Flow<Resource<List<Task>>>
    suspend fun saveTask(task: Task):Response<Task>
    suspend fun deleteTask(id: Int)
}
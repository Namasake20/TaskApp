package com.namasake.task.feature_task.doman.use_case

import com.namasake.task.feature_task.core.util.Resource
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.doman.repo.MainRepo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class UseCase(private val repo: MainRepo) {
    fun fetchTasks(): Flow<Resource<List<Task>>> {
        return repo.getTasks()
    }
    suspend fun  saveNewTask(task: Task): Response<Task> {
        return repo.saveTask(task)
    }

    suspend fun deleteTask(id: Int) = repo.deleteTask(id)
}
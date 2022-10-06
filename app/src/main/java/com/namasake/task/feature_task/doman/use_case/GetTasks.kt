package com.namasake.task.feature_task.doman.use_case

import com.namasake.task.feature_task.core.util.Resource
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.doman.repo.MainRepo
import kotlinx.coroutines.flow.Flow

class GetTasks(private val repo: MainRepo) {
    operator fun invoke(): Flow<Resource<List<Task>>> {
        return repo.getTasks()
    }
}
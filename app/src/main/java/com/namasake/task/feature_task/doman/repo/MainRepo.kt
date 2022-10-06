package com.namasake.task.feature_task.doman.repo

import com.namasake.task.feature_task.core.util.Resource
import com.namasake.task.feature_task.doman.model.Task
import kotlinx.coroutines.flow.Flow

interface MainRepo {
    fun getTasks(): Flow<Resource<List<Task>>>
}
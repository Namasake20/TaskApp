package com.namasake.task.feature_task.data.remote

import com.namasake.task.feature_task.data.local.TaskEntity
import com.namasake.task.feature_task.doman.model.Task

data class TaskDto(
    val completed: Boolean,
    val id: Int,
    val title: String
){
    fun toTaskEntity():TaskEntity{
        return TaskEntity(
            completed = completed,
            id = id,
            title = title
        )
    }
}
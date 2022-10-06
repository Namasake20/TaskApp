package com.namasake.task.feature_task.data.remote

import com.namasake.task.feature_task.data.remote.TaskDto
import retrofit2.Response
import retrofit2.http.GET

interface TaskApi {

    @GET("/api/v1/tasks")
    suspend fun getTasks():List<TaskDto>?

    companion object {
        const val BASE_URL = "http://taskapi-env.eba-nvskuape.us-east-1.elasticbeanstalk.com"
    }
}
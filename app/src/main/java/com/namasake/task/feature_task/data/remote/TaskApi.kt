package com.namasake.task.feature_task.data.remote

import com.namasake.task.feature_task.doman.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @GET("/api/v1/tasks")
    suspend fun getTasks():List<TaskDto>?

    @POST("/api/v1/task/save")
    suspend fun saveTask(@Body task: Task): Response<Task>

    @DELETE("/api/v1/{taskId}")
    suspend fun deleteTask(@Path("taskId") id: Int)

    @PUT("/api/v1/")
    suspend fun updateTask()

    companion object {
        const val BASE_URL = "http://taskapi-env.eba-nvskuape.us-east-1.elasticbeanstalk.com"
    }
}
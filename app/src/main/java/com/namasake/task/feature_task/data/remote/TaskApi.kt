package com.namasake.task.feature_task.data.remote

import com.namasake.task.feature_task.doman.model.Task
import retrofit2.Response
import retrofit2.http.*

interface TaskApi {

    @GET("/api/v1/tasks")
    suspend fun getTasks():List<TaskDto>?

    @POST("/api/v1/task/save")
    suspend fun saveTask(@Body task: Task): Response<Task>

    @DELETE("/api/v1/{taskId}")
    suspend fun deleteTask(@Path("taskId") id: Int)

    @PUT("/api/v1/{taskId}")
    suspend fun updateTask(@Path("taskId") id: Int, @Query("completed") completed:Boolean):Response<Task>

    companion object {
        const val BASE_URL = "http://taskapi-env.eba-nvskuape.us-east-1.elasticbeanstalk.com"
    }
}
package com.namasake.task

import retrofit2.Response
import retrofit2.http.GET

interface TaskApi {

    @GET("/api/v1/tasks")
    suspend fun getTasks():Response<List<Task>>
}
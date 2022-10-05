package com.namasake.task

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api:TaskApi by lazy {
        Retrofit.Builder().baseUrl("http://taskapi-env.eba-nvskuape.us-east-1.elasticbeanstalk.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApi::class.java)
    }
}
package com.namasake.task.feature_task.di

import android.app.Application
import androidx.room.Room
import com.namasake.task.feature_task.data.local.TaskDatabase
import com.namasake.task.feature_task.data.remote.TaskApi
import com.namasake.task.feature_task.data.remote.TaskApi.Companion.BASE_URL
import com.namasake.task.feature_task.data.repo.MainRepoImpl
import com.namasake.task.feature_task.doman.repo.MainRepo
import com.namasake.task.feature_task.doman.use_case.GetTasks
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {

    @Singleton
    @Provides
    fun provideTaskApi(): TaskApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepo(api: TaskApi,db:TaskDatabase): MainRepo = MainRepoImpl(api, dao = db.taskDao())

    @Singleton
    @Provides
    fun provideDatabase(application: Application): TaskDatabase =
        Room.databaseBuilder(application, TaskDatabase::class.java, "task_database").build()

    @Singleton
    @Provides
    fun provideGetPredictionUseCase(repository: MainRepo):GetTasks{
        return GetTasks(repository)
    }
}
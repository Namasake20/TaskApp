package com.namasake.task.feature_task.data.repo

import com.namasake.task.feature_task.core.util.Resource
import com.namasake.task.feature_task.data.local.TaskDao
import com.namasake.task.feature_task.data.remote.TaskApi
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.doman.repo.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MainRepoImpl @Inject constructor(
    private val api: TaskApi,
    private val dao: TaskDao
): MainRepo {
    override fun getTasks(): Flow<Resource<List<Task>>> = flow {

        emit(Resource.Loading())

        try {
            val remoteTask = api.getTasks()
            dao.deleteTasks()
            if (remoteTask != null){
                dao.insertTasks(remoteTask.map {it.toTaskEntity() })
            }

        }catch (e: HttpException){
            //something wrong

        }catch (e: IOException){

//            emit(Resource.Error(message = "Error,check internet connection",data = prediction))
        }

        //emitting new tasks
        val newTasks = dao.getAllTasks().map { it.toTask() }
        emit(Resource.Success(newTasks))

    }

    override suspend fun saveTask(task: Task): Response<Task> {
        return api.saveTask(task)
    }

    override suspend fun deleteTask(id: Int) {
        api.deleteTask(id)
    }

}
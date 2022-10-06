package com.namasake.task.feature_task.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM taskentity")
    suspend fun deleteTasks()

    @Query("SELECT * FROM taskentity")
    fun getAllTasks(): List<TaskEntity>

}
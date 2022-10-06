package com.namasake.task.feature_task.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.namasake.task.feature_task.doman.model.Task

@Entity
data class TaskEntity (
        val completed: Boolean,
        @PrimaryKey val id: Int,
        val title: String

        ){

        fun toTask(): Task {
                return Task(
                        completed = completed,
                        id = id,
                        title = title
                )
        }
}
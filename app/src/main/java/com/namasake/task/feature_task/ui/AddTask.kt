package com.namasake.task.feature_task.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.namasake.task.databinding.ActivityAddTaskBinding
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.presentation.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTask : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private val newViewModel:TaskViewModel by viewModels()
    private val randomInt = (10..30).random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSaveTask.setOnClickListener {
            if (!validateTask()){
                return@setOnClickListener
            }
            else{
                val data = Task(false,randomInt, binding.edtTitle.editText?.text.toString())
                newViewModel.saveNewTask(data)
                saveData()
            }
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

    }

    private fun saveData(){
        newViewModel.saveResponse.observe(this, Observer {  response ->
            if (response.isSuccessful){
                Log.d("AddTask",response.body().toString())
                Log.d("AddTask",response.code().toString())
                Log.d("AddTask",response.message())
            }
            else{
                Log.e("AddTask",response.errorBody().toString())
            }
        })
    }

    fun validateTask():Boolean{
        if(binding.edtTitle.editText?.text?.isBlank() == true){
            binding.edtTitle.error = "Enter task title"
            return false
        }
        else{
            binding.edtTitle.isErrorEnabled = false
            binding.edtTitle.error = null
            return true
        }
    }


}



package com.namasake.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.namasake.task.databinding.ActivityMainBinding
import com.namasake.task.feature_task.data.remote.TaskDto
import com.namasake.task.feature_task.presentation.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import java.util.logging.Level.INFO
import kotlin.collections.ArrayList

const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private val viewModel:TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getNewTask()
                observeData()
            }
        }
    }

    private suspend fun observeData() {
        viewModel.task.collect{ event ->
            when(event){
                is TaskViewModel.TaskEvent.Success -> {
                    binding.progressBar.isVisible = false
                    binding.rvTasks.apply {
                        taskAdapter = TaskAdapter()
                        adapter = taskAdapter
                        layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                    Log.i(TAG,event.result.toString())
                    taskAdapter.tasks = event.result!!

                }
                is TaskViewModel.TaskEvent.Loading ->{
                    binding.progressBar.isVisible = true
                }
                is TaskViewModel.TaskEvent.Failure -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, "Something wrong: "+event.error, Toast.LENGTH_SHORT).show()
                    Log.e(TAG,event.error)
                }
                is TaskViewModel.TaskEvent.Empty -> Unit

            }

        }

    }
}

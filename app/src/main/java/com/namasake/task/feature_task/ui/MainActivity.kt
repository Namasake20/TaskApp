package com.namasake.task.feature_task.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.namasake.task.databinding.ActivityMainBinding
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.presentation.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        binding.fabAdd.setOnClickListener {
            Intent(this, AddTask::class.java).also {
                startActivity(it)
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
                        val swipeGesture = object : SwipeGesture(){
                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                val deletedTask:Task = taskAdapter.tasks[viewHolder.adapterPosition]
                                lifecycleScope.launch {
                                    viewModel.deleteTask(deletedTask.id)
                                    val mySnackbar = Snackbar.make(binding.rvTasks,"Deleted "+deletedTask.title,Snackbar.LENGTH_LONG)
                                    val textView:TextView = mySnackbar.view.findViewById(com.google.android.material.R.id.snackbar_action);
                                    textView.isAllCaps = false
                                    mySnackbar.setAction("Undo"
                                    ) {
                                        viewModel.saveNewTask(
                                            Task(
                                                false,
                                                deletedTask.id,
                                                deletedTask.title)
                                            )
                                    }.show()
                                    taskAdapter.notifyDataSetChanged()
                                    viewModel.getNewTask()

                                }
                            }
                        }
                        val touchhelper = ItemTouchHelper(swipeGesture)
                        touchhelper.attachToRecyclerView(binding.rvTasks)
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

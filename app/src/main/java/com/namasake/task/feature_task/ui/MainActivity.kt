package com.namasake.task.feature_task.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.namasake.task.R
import com.namasake.task.databinding.ActivityMainBinding
import com.namasake.task.databinding.TastItemBinding
import com.namasake.task.feature_task.doman.model.Task
import com.namasake.task.feature_task.presentation.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),TaskAdapter.OnTaskClickListener {
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
                        taskAdapter = TaskAdapter(this@MainActivity)
                        adapter = taskAdapter
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        val swipeGesture = object : SwipeGesture(){
                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                val deletedTask:Task = taskAdapter.tasks[viewHolder.adapterPosition]
                                lifecycleScope.launch {
                                    try {
                                        viewModel.deleteTask(deletedTask.id)

                                        val mySnackbar = Snackbar.make(binding.rvTasks,"Deleted "+deletedTask.title,Snackbar.LENGTH_LONG)
                                        val textView:TextView = mySnackbar.view.findViewById(com.google.android.material.R.id.snackbar_action);
                                        textView.isAllCaps = false
                                        val color = R.color.teal_200
                                        textView.setTextColor(color)
                                        mySnackbar.setAction("Undo") { viewModel.saveNewTask(
                                            Task(
                                                false,
                                                deletedTask.id,
                                                deletedTask.title)
                                        )
                                        }.show()
                                        taskAdapter.notifyDataSetChanged()
                                        viewModel.getNewTask()
                                    }
                                    catch (e:IOException){
                                        Snackbar.make(binding.rvTasks,"No internet",Snackbar.LENGTH_SHORT).show()
                                    }
                                    catch (e:HttpException){
                                        Snackbar.make(binding.rvTasks,"Error",Snackbar.LENGTH_SHORT).show()
                                    }

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

    override fun onTaskClickListener(task: Task, position: Int) {
        lifecycleScope.launch {
            try {
                viewModel.updateTask(task.id,true)
            }catch (e:HttpException){
                Toast.makeText(parent, "error", Toast.LENGTH_SHORT).show()
            }catch (e:HttpException){
                Toast.makeText(parent, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        if (checkInternet(this)){
//            Toast.makeText(this, "Iko", Toast.LENGTH_SHORT).show()
        }else{
            Snackbar.make(binding.root,"No Internet Connection",Snackbar.LENGTH_LONG).show()
        }
        super.onResume()
    }
    fun checkInternet(context: Context):Boolean{
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val net = connManager.activeNetwork ?: return false
            val activeNet = connManager.getNetworkCapabilities(net) ?: return false

            return when{
                activeNet.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNet.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        }else{
            @Suppress("DEPRECATION")
            val newtworkInfo = connManager.activeNetworkInfo ?: return false
            return newtworkInfo.isConnected
        }

    }
}

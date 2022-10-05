package com.namasake.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.namasake.task.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTasks()

            } catch (e: IOException){
                Log.e(TAG, "IOException,you might not have internet connection" )
                binding.progressBar.isVisible = false
                return@launchWhenCreated

            }
            catch (e: HttpException){
                Log.e(TAG, "HttpException,unexpected response" )
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null){
                taskAdapter.tasks = response.body()!!
            }
            else{
                Log.e(TAG, "something wrong" )

            }
            binding.progressBar.isVisible = false
        }
    }

    private fun setUpRecyclerView() = binding.rvTasks.apply {
        taskAdapter = TaskAdapter()
        adapter = taskAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)

    }
}
package com.namasake.task.feature_task.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.namasake.task.databinding.TastItemBinding
import com.namasake.task.feature_task.doman.model.Task

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder> () {

    inner class TaskViewHolder(val binding: TastItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this,diffCallback)

    var tasks: List<Task>
        get() = differ.currentList
        set(value) {differ.submitList(value)}


    fun getId(position: Int):Int{
        return tasks.get(position).id
    }
    fun getTitle(position: Int):String{
        return tasks.get(position).title
    }

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(TastItemBinding.inflate(
            LayoutInflater.from(parent.context
        ),parent,false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.apply {
            val task = tasks[position]
            tvTitle.text = task.title
            cbDone.isChecked = task.completed

        }
    }

}
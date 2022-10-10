package com.namasake.task.feature_task.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.namasake.task.databinding.TastItemBinding
import com.namasake.task.feature_task.doman.model.Task

class TaskAdapter(private val taskClickListener:OnTaskClickListener): RecyclerView.Adapter<TaskAdapter.TaskViewHolder> () {

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


    interface OnTaskClickListener{
        fun onTaskClickListener(task: Task, position: Int)

    }

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binder = TastItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = TaskViewHolder(binder)
        binder.cbDone.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION } ?: return@setOnClickListener
            taskClickListener.onTaskClickListener(tasks[position], position)
        }
        return holder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.binding.apply {
            val task = tasks[position]
            tvTitle.text = task.title
            cbDone.isChecked = task.completed

        }
    }

}
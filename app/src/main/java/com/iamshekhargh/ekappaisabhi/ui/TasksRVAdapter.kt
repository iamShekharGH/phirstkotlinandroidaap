package com.iamshekhargh.ekappaisabhi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iamshekhargh.ekappaisabhi.databinding.ItemTaskBinding
import com.iamshekhargh.ekappaisabhi.models.Task

/**
 * Created by <<-- iamShekharGH -->>
 * on 31 March 2021
 * at 5:20 PM.
 */
class TasksRVAdapter(val listener: OnItemClickListener) :
    ListAdapter<Task, TasksRVAdapter.TasksVH>(DiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksVH {
        val b = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksVH(b)
    }

    override fun onBindViewHolder(holder: TasksVH, position: Int) {
        holder.binde(getItem(position))
    }

    inner class TasksVH(val b: ItemTaskBinding) : RecyclerView.ViewHolder(b.root) {
        init {
            b.apply {
                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(getItem(adapterPosition))
                    }
                }
                itemCheckbox.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onCheckBoxClicked(getItem(adapterPosition), itemCheckbox.isChecked)
                    }
                }
//                root.setOnClickListener(
//                    if (adapterPosition != NO_POSITION) {
//                        listener.onItemClicked(getItem(adapterPosition))
//                    }
//                )

            }
        }

        fun binde(t: Task) {
            b.apply {
                itemCheckbox.isChecked = t.completed

                itemTextviewTask.text = t.name
                itemTextviewTask.paint.isStrikeThruText = t.completed
                itemImageviewImportant.isVisible = t.important
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(t: Task)
        fun onCheckBoxClicked(t: Task, isChecked: Boolean)
    }

    class DiffUtilItemCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }


}
package com.example.todoapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.common.Utils
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.databinding.LayoutListItemBinding

class TodoAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val todoList = mutableListOf<TodoItem>()

    fun updateList(list: List<TodoItem>) {
        val diffResult = DiffUtil.calculateDiff(TodoDiffUtils(todoList, list))
        todoList.clear()
        todoList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addAItemToList(item: TodoItem) {
        todoList.add(item)
        notifyItemInserted(todoList.size - 1)
    }
    fun addAllItemToList(list: List<TodoItem>) {
        val diffResult = DiffUtil.calculateDiff(TodoDiffUtils(todoList, list))
        todoList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    interface TodoListener {
        fun onTodoItemClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Utils.TodoStatus.PENDING.ordinal -> {
                ItemViewHolder(
                    LayoutListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            Utils.TodoStatus.COMPLETED.ordinal -> {
                ItemViewHolder(
                    LayoutListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                ItemViewHolder(
                    LayoutListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    inner class ItemViewHolder(itemView: LayoutListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private var itemRowBinding = itemView
        fun bind(item: TodoItem) {
            if (item != null) {
                itemRowBinding.titleTextView.text = item.task
                itemRowBinding.completedCheckBox.isChecked =
                    item.status == Utils.TodoStatus.COMPLETED
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return (todoList[position].id % 2).toInt()
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            Utils.TodoStatus.PENDING.ordinal -> {
                val item = todoList[position]
                val itemViewHolder = holder as ItemViewHolder
                itemViewHolder.bind(item)
            }

            Utils.TodoStatus.COMPLETED.ordinal -> {
                val item = todoList[position]
                val itemViewHolder = holder as ItemViewHolder
                itemViewHolder.bind(item)
            }

            else -> {
                val item = todoList[position]
                val itemViewHolder = holder as ItemViewHolder
                itemViewHolder.bind(item)
            }
        }
    }
}

class TodoDiffUtils(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
            oldList[oldItemPosition].task == newList[newItemPosition].task &&
                oldList[oldItemPosition].status == newList[newItemPosition].status
            )
    }
}

package com.todo.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.todo.common.Constant.Companion.PENDING
import com.todo.common.Constant.Companion.TODO_STATUS
import com.todo.common.Constant.Companion.TODO_TABLE_NAME
import com.todo.common.Constant.Companion.TODO_TASK
import com.todo.common.Utils.TodoStatus
import com.todo.domain.interfaces.models.ITodoItem

@Entity(tableName = TODO_TABLE_NAME)
data class TodoItem(

    @PrimaryKey
    @SerializedName("id")
    override val id: Long,

    @ColumnInfo(name = TODO_TASK)
    @SerializedName("title")
    override val task: String,

    @ColumnInfo(name = TODO_STATUS, defaultValue = PENDING)
    override val status: TodoStatus = TodoStatus.PENDING
) : ITodoItem

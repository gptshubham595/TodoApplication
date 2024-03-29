package com.todo.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.todo.common.PENDING
import com.todo.common.TODO_STATUS
import com.todo.common.TODO_TABLE_NAME
import com.todo.common.TODO_TASK
import com.todo.common.Utils.TodoStatus

@Entity(tableName = TODO_TABLE_NAME)
data class TodoItemEntity(

    @PrimaryKey
    @SerializedName("id")
    val id: Long,

    @ColumnInfo(name = TODO_TASK)
    @SerializedName("title")
    val task: String,

    @ColumnInfo(name = TODO_STATUS, defaultValue = PENDING)
    val status: TodoStatus = TodoStatus.PENDING
)

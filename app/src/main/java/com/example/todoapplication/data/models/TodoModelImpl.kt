package com.example.todoapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapplication.common.Constant.Companion.PENDING
import com.example.todoapplication.common.Constant.Companion.TODO_STATUS
import com.example.todoapplication.common.Constant.Companion.TODO_TABLE_NAME
import com.example.todoapplication.common.Constant.Companion.TODO_TASK
import com.example.todoapplication.common.Utils.TodoStatus
import com.example.todoapplication.domain.interfaces.models.ITodoItem
import com.google.gson.annotations.SerializedName

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
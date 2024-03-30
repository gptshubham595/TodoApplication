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
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PersistedName
import javax.inject.Inject

@Entity(tableName = TODO_TABLE_NAME)
data class TodoItemEntity(
    @PrimaryKey
    @SerializedName("id")
    var id: Long,

    @ColumnInfo(name = TODO_TASK)
    @SerializedName("title")
    var task: String,

    @ColumnInfo(name = TODO_STATUS, defaultValue = PENDING)
    var status: String = TodoStatus.PENDING.name
) {
    fun toRealm(): TodoItemEntityRealm {
        return TodoItemEntityRealm(id, task, status)
    }
}

open class TodoItemEntityRealm : RealmObject {
    fun toTodoItemEntity(): TodoItemEntity {
        return TodoItemEntity(id, task, status)
    }

    @field:PrimaryKey
    @SerializedName("id")
    var id: Long = 0L

    @ColumnInfo(name = TODO_TASK)
    @SerializedName("title")
    var task: String = ""

    @PersistedName(TODO_STATUS)
    @ColumnInfo(name = TODO_STATUS, defaultValue = PENDING)
    var status: String = TodoStatus.PENDING.name

    constructor() {
        id = 0
        task = ""
        status = TodoStatus.PENDING.name
    }

    constructor(id: Long, task: String, status: TodoStatus) {
        this.id = id
        this.task = task
        this.status = status.name
    }

    @Inject
    constructor(id: Long, task: String, status: String) {
        this.id = id
        this.task = task
        this.status = status
    }
}

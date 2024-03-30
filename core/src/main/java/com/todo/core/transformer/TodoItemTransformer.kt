package com.todo.core.transformer

import com.todo.common.Utils
import com.todo.data.models.TodoItemEntity
import com.todo.domain.models.TodoItem

fun TodoItemEntity.toDomain(): TodoItem {
    return TodoItem(
        id = this.id ?: 0L,
        task = this.task ?: "",
        status = this.status ?: Utils.TodoStatus.PENDING.name
    )
}

fun TodoItem.toData(): TodoItemEntity {
    return TodoItemEntity(
        id = this.id ?: 0,
        task = this.task ?: "",
        status = this.status ?: Utils.TodoStatus.PENDING.name
    )
}

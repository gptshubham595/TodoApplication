package com.todo.core.transformer

import com.todo.data.models.TodoItemEntity
import com.todo.domain.models.TodoItem

fun TodoItemEntity.toDomain(): TodoItem {
    return TodoItem(
        id = this.id,
        task = this.task,
        status = this.status
    )
}

fun TodoItem.toData(): TodoItemEntity {
    return TodoItemEntity(
        id = this.id,
        task = this.task,
        status = this.status
    )
}

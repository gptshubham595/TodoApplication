package com.todo.core.database.realmDB

import com.todo.data.interfaces.TodoDataSource
import com.todo.data.models.TodoItemEntity
import com.todo.data.models.TodoItemEntityRealm
import io.realm.kotlin.Realm
import javax.inject.Inject
import kotlin.reflect.KClass

class TodoRealmDaoImpl @Inject constructor(
    override val realm: Realm
) : TodoRealmDao, TodoDataSource {

    override val clazz: KClass<TodoItemEntityRealm> = TodoItemEntityRealm::class
    override suspend fun fetchAllTodoItems(): List<TodoItemEntity> {
        return findAll().map { it.toTodoItemEntity() }
    }

    override suspend fun addTodoItem(todoItemEntity: TodoItemEntity): Long {
        insert(todoItemEntity.toRealm())
        return 1
    }

    override suspend fun deleteTodoItem(todoId: Long): Int {
        findById(todoId.toString())?.let { delete(it) }
        return 1
    }

    override suspend fun updateTodoItem(todoItemEntity: TodoItemEntity): Int {
        update(todoItemEntity.toRealm())
        return 1
    }

    override suspend fun fetchIdTodoItem(todoId: Int): TodoItemEntity? {
        return findById(todoId.toString())?.toTodoItemEntity()
    }
}

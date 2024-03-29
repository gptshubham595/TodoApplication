package com.todo.core.database.cached.sharedPref

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.todo.core.database.interfaces.TodoDataSource
import com.todo.data.models.TodoItemEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefTodoDataSource @Inject constructor(
    @ApplicationContext applicationContext: Context,
    private val gson: Gson
) : TodoDataSource {

    // create a encyrpterd shared pref
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val sharedPreferences = EncryptedSharedPreferences.create(
        // passing a file name to share a preferences
        "preferences",
        masterKeyAlias,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun fetchAllTodoItems(): List<TodoItemEntity> {
        val todoItemEntities = mutableListOf<TodoItemEntity>()
        sharedPreferences.all.forEach {
            val todoItem = sharedPreferences.getString(it.key, null)
            todoItem?.let {
                val data: TodoItemEntity = gson.fromJson(it, TodoItemEntity::class.java)
                todoItemEntities.add(data)
            }
        }
        return todoItemEntities
    }

    override suspend fun addTodoItem(todoItemEntity: TodoItemEntity): Long {
        val data = gson.toJson(todoItemEntity)
        sharedPreferences.edit().putString(todoItemEntity.id.toString(), data).apply()
        return 1
    }

    override suspend fun deleteTodoItem(todoId: Long): Int {
        sharedPreferences.edit().remove(todoId.toString()).apply()
        return 1
    }

    override suspend fun updateTodoItem(todoItemEntity: TodoItemEntity): Int {
        val data = gson.toJson(todoItemEntity)
        sharedPreferences.edit().putString(todoItemEntity.id.toString(), data).apply()
        return 1
    }
}

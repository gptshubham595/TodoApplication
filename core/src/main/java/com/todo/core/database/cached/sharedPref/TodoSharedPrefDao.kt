package com.todo.core.database.cached.sharedPref

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.todo.core.database.interfaces.ITodoDB
import com.todo.data.models.TodoItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TodoSharedPrefDao @Inject constructor(
    @ApplicationContext applicationContext: Context,
    private val gson: Gson
) : ITodoDB {

    //create a encyrpterd shared pref
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val sharedPreferences = EncryptedSharedPreferences.create(
        // passing a file name to share a preferences
        "preferences",
        masterKeyAlias,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun fetchAllTodoItems(): List<TodoItem> {
        val todoItems = mutableListOf<TodoItem>()
        sharedPreferences.all.forEach {
            val todoItem = sharedPreferences.getString(it.key, null)
            todoItem?.let {
                val data: TodoItem = gson.fromJson(it, TodoItem::class.java)
                todoItems.add(data)
            }
        }
        return todoItems
    }

    override suspend fun addTodoItem(todoItem: TodoItem): Long {
        val data = gson.toJson(todoItem)
        sharedPreferences.edit().putString(todoItem.id.toString(), data).apply()
        return 1
    }

    override suspend fun deleteTodoItem(todoId: Long): Int {
        sharedPreferences.edit().remove(todoId.toString()).apply()
        return 1
    }

    override suspend fun updateTodoItem(todoItem: TodoItem): Int {
        val data = gson.toJson(todoItem)
        sharedPreferences.edit().putString(todoItem.id.toString(), data).apply()
        return 1
    }
}

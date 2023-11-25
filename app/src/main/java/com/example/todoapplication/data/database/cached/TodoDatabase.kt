package com.example.todoapplication.data.database.cached

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapplication.data.models.TodoItem

@Database(entities = [TodoItem::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao
}
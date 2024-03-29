package com.todo.core.database.cached.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.todo.data.models.TodoItemEntity

@Database(entities = [TodoItemEntity::class], version = 1)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoRoomDao
}

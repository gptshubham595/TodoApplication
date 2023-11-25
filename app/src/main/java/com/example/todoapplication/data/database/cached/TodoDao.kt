package com.example.todoapplication.data.database.cached

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapplication.data.models.TodoItem

@Dao
interface TodoDao {

    @Query("Select * from todo_table")
    suspend fun fetchAllTodoItems(): List<TodoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItem: TodoItem): Long

    @Query("Delete from todo_table where id = :todoId")
    suspend fun deleteTodoItem(todoId: Long): Int

    @Update
    suspend fun updateTodoItem(todoItem: TodoItem): Int


}
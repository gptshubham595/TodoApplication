package com.todo.core.database.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.todo.data.interfaces.TodoDataSource
import com.todo.data.models.TodoItemEntity

@Dao
interface TodoRoomDao : TodoDataSource {

    @Query("Select * from todo_table")
    override suspend fun fetchAllTodoItems(): List<TodoItemEntity>

    @Query("Select * from todo_table where id = :todoId")
    override suspend fun fetchIdTodoItem(todoId: Int): TodoItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun addTodoItem(todoItemEntity: TodoItemEntity): Long

    @Query("Delete from todo_table where id = :todoId")
    override suspend fun deleteTodoItem(todoId: Long): Int

    @Update
    override suspend fun updateTodoItem(todoItemEntity: TodoItemEntity): Int
}

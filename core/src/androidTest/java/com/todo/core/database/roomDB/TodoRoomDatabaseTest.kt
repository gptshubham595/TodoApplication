package com.todo.core.database.roomDB

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.todo.common.Utils
import com.todo.data.models.TodoItemEntity
import io.realm.kotlin.internal.platform.runBlocking
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TodoDatabaseTest {

    private lateinit var todoDatabase: TodoRoomDatabase

    // @Before annotation is used to use the method before each test case
    // The @Before method is executed before each test method, where we initialize an in-memory instance of the Room Database.

    @Before
    fun setup() {
        // Initialize Room Database in memory for testing
        todoDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoRoomDatabase::class.java
        ).build()
    }

    // @After annotation is used to use the method after each test case
    @After
    fun teardown() {
        // Close the Room Database after testing
        todoDatabase.close()
    }

    @Test
    fun insertTodoItem() {
        // Write test cases to insert and retrieve data from the Room Database
        // Verify that the data is inserted correctly and can be retrieved as expected
        var retrievedItem: TodoItemEntity? = null
        // Create a sample todo item
        val todoItem = TodoItemEntity(
            id = 1,
            task = "Test Todo",
            status = Utils.TodoStatus.PENDING.name
        )
        runBlocking {
            // Insert the todo item into the Room Database
            todoDatabase.getTodoDao().addTodoItem(todoItem)

            // Retrieve the todo item from the Room Database
            retrievedItem = todoDatabase.getTodoDao().fetchIdTodoItem(1)
        }
        // Verify that the retrieved item is not null
        assertNotNull(retrievedItem)

        // Verify that the retrieved item matches the inserted item
        assertEquals(todoItem.id, retrievedItem?.id)
        assertEquals(todoItem.task, retrievedItem?.task)
        assertEquals(todoItem.status, retrievedItem?.status)
    }

    // Add more test methods for other CRUD operations (update, delete, query) as needed
    @Test
    fun updateTodoItem() {
        // Write test cases to update and retrieve data from the Room Database
        // Verify that the data is updated correctly and can be retrieved as expected
        var retrievedItem: TodoItemEntity? = null
        // Create a sample todo item
        val todoItem = TodoItemEntity(
            id = 2,
            task = "Test Todo",
            status = Utils.TodoStatus.PENDING.name
        )
        runBlocking {
            // Insert the todo item into the Room Database
            todoDatabase.getTodoDao().addTodoItem(todoItem)
        }

        // Update the status of the todo item
        val updatedTodoItem = todoItem.copy(status = Utils.TodoStatus.COMPLETED.name)
        runBlocking {
            // Update the todo item in the Room Database
            todoDatabase.getTodoDao().updateTodoItem(updatedTodoItem)
            // Retrieve the updated todo item from the Room Database
            retrievedItem = todoDatabase.getTodoDao().fetchIdTodoItem(2)
        }
        // Verify that the retrieved item is not null
        assertNotNull(retrievedItem)

        // Verify that the retrieved item matches the updated item
        assertEquals(updatedTodoItem.id, retrievedItem?.id)
        assertEquals(updatedTodoItem.task, retrievedItem?.task)
        assertEquals(updatedTodoItem.status, retrievedItem?.status)
    }

    @Test
    fun deleteTodoItem() {
        // Write test cases to delete and retrieve data from the Room Database
        // Verify that the data is deleted correctly and cannot be retrieved after deletion
        var retrievedItem: TodoItemEntity? = null
        // Create a sample todo item
        val todoItem = TodoItemEntity(
            id = 3,
            task = "Test Todo",
            status = Utils.TodoStatus.PENDING.name
        )
        runBlocking {
            // Insert the todo item into the Room Database
            todoDatabase.getTodoDao().addTodoItem(todoItem)
        }

        runBlocking {
            // Delete the todo item from the Room Database
            todoDatabase.getTodoDao().deleteTodoItem(3)
            // Retrieve the deleted todo item from the Room Database
            retrievedItem = todoDatabase.getTodoDao().fetchIdTodoItem(3)
        }
        // Verify that the retrieved item is null after deletion
        assertEquals(null, retrievedItem)
    }

    @Test
    fun fetchAllTodoItems() {
        // Write test cases to fetch all todo items from the Room Database
        // Verify that the data is fetched correctly and the list is not empty
        var todoItems: List<TodoItemEntity>? = null
        // Create sample todo items
        val todoItem1 = TodoItemEntity(
            id = 4,
            task = "Test Todo 1",
            status = Utils.TodoStatus.PENDING.name
        )
        val todoItem2 = TodoItemEntity(
            id = 5,
            task = "Test Todo 2",
            status = Utils.TodoStatus.COMPLETED.name
        )
        runBlocking {
            // Insert the todo items into the Room Database
            todoDatabase.getTodoDao().addTodoItem(todoItem1)
            todoDatabase.getTodoDao().addTodoItem(todoItem2)
            // Fetch all todo items from the Room Database
            todoItems = todoDatabase.getTodoDao().fetchAllTodoItems()
        }
        // Verify that the list of todo items is not empty
        assertNotNull(todoItems)
        // Verify that the list contains the inserted todo items
        assertEquals(2, todoItems?.size)
        assertEquals(todoItem1.id, todoItems?.get(0)?.id)
        assertEquals(todoItem1.task, todoItems?.get(0)?.task)
        assertEquals(todoItem1.status, todoItems?.get(0)?.status)
        assertEquals(todoItem2.id, todoItems?.get(1)?.id)
        assertEquals(todoItem2.task, todoItems?.get(1)?.task)
        assertEquals(todoItem2.status, todoItems?.get(1)?.status)
    }
}

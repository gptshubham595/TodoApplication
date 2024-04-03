package com.todo.core.di.module

import android.content.Context
import com.google.gson.Gson
import com.todo.core.database.realmDB.TodoRealmDaoImpl
import com.todo.core.database.roomDB.TodoRoomDatabase
import com.todo.core.database.sharedPref.SharedPrefTodoDataSource
import com.todo.core.di.qualifier.RealmDBQualifier
import com.todo.core.di.qualifier.RoomDatabaseQualifier
import com.todo.core.di.qualifier.SharedPrefDatabaseQualifier
import com.todo.data.interfaces.TodoDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    @Singleton
    @RoomDatabaseQualifier
    fun provideTodoRoomDao(todoRoomDatabase: TodoRoomDatabase): TodoDataSource {
        return todoRoomDatabase.getTodoDao()
    }

    @Provides
    @Singleton
    @RealmDBQualifier
    fun provideRealmDao(todoRealmDaoImpl: TodoRealmDaoImpl): TodoDataSource {
        return todoRealmDaoImpl
    }

    @Provides
    @Singleton
    @SharedPrefDatabaseQualifier
    fun provideSharedPrefTodoDataSource(@ApplicationContext context: Context, gson: Gson): TodoDataSource {
        return SharedPrefTodoDataSource(context, gson)
    }
}

package com.todo.core.di.module

import com.todo.core.database.cached.realmDB.TodoRealmDao
import com.todo.core.database.cached.realmDB.TodoRealmDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DaoModule {
    @Binds
    abstract fun bindTodoRealmDao(todoRealmDaoImpl: TodoRealmDaoImpl): TodoRealmDao
}

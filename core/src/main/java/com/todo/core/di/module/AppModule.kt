package com.todo.core.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.todo.common.APIConstants.BASE_URl
import com.todo.common.TODO_DATABASE_NAME
import com.todo.core.database.api.ApiInterceptor
import com.todo.core.database.api.ApiInterface
import com.todo.core.database.cached.realmDB.TodoRealmDaoImpl
import com.todo.core.database.cached.roomDB.TodoRoomDatabase
import com.todo.core.database.cached.sharedPref.SharedPrefTodoDataSource
import com.todo.core.database.interfaces.TodoDataSource
import com.todo.core.di.qualifier.RealmDBQualifier
import com.todo.core.di.qualifier.RoomDatabaseQualifier
import com.todo.core.di.qualifier.SharedPrefDatabaseQualifier
import com.todo.core.repositories.TodoRepositoryImpl
import com.todo.data.models.TodoItemEntityRealm
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.usecases.AddTodoItemUseCase
import com.todo.domain.usecases.GetTodoItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoRoomDataBase(application: Application): TodoRoomDatabase {
        return Room.databaseBuilder(application, TodoRoomDatabase::class.java, TODO_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRealm(@ApplicationContext context: Context): Realm {
        val realmConfig = RealmConfiguration.Builder(
            schema = setOf(
                TodoItemEntityRealm::class
            )
        ).name("todo_realm.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        return Realm.open(realmConfig)
    }

    @Provides
    @Singleton
    fun provideApiInterceptor(): ApiInterceptor {
        return ApiInterceptor()
    }

    @Provides
    @Singleton
    fun provideApiInterface(interceptor: ApiInterceptor): ApiInterface {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(
            ApiInterface::class.java
        )
    }

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
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    @SharedPrefDatabaseQualifier
    fun provideSharedPrefTodoDataSource(@ApplicationContext context: Context, gson: Gson): TodoDataSource {
        return SharedPrefTodoDataSource(context, gson)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(@RealmDBQualifier todoDao: TodoDataSource, apiInterface: ApiInterface): TodoRepository {
        return TodoRepositoryImpl(todoDao, apiInterface)
    }

    @Provides
    @Singleton
    fun provideGetTodoItemsUseCase(todoRepositoryImpl: TodoRepository): GetTodoItemsUseCase {
        return GetTodoItemsUseCase(todoRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideAddTodoItemUseCase(todoRepositoryImpl: TodoRepository): AddTodoItemUseCase {
        return AddTodoItemUseCase(todoRepositoryImpl)
    }

    @Provides
    fun providesTodoItemEntityRealm(): TodoItemEntityRealm {
        return TodoItemEntityRealm(0, "", "")
    }
}

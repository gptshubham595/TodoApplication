package com.todo.core.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.todo.common.APIConstants.BASE_URl
import com.todo.common.TODO_DATABASE_NAME
import com.todo.core.database.roomDB.TodoRoomDatabase
import com.todo.core.di.qualifier.RoomDatabaseQualifier
import com.todo.core.network.api.ApiInterceptor
import com.todo.core.network.api.ApiService
import com.todo.core.repositories.TodoRepositoryImpl
import com.todo.data.interfaces.TodoDataSource
import com.todo.data.models.TodoItemEntityRealm
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.usecases.AddTodoItemUseCase
import com.todo.domain.usecases.GetTodoItemsUseCase
import com.todo.domain.usecases.UpdateTodoItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
    fun provideRealm(): Realm {
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
    fun provideApiInterface(interceptor: ApiInterceptor, @ApplicationContext context: Context): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(
            ApiService::class.java
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(@RoomDatabaseQualifier todoDao: TodoDataSource, apiService: ApiService): TodoRepository {
        return TodoRepositoryImpl(todoDao, apiService)
    }

    @Provides
    @Singleton
    fun provideGetTodoItemsUseCase(todoRepository: TodoRepository): GetTodoItemsUseCase {
        return GetTodoItemsUseCase(todoRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTodoItemsUseCase(todoRepository: TodoRepository): UpdateTodoItemsUseCase {
        return UpdateTodoItemsUseCase(todoRepository)
    }

    @Provides
    @Singleton
    fun provideAddTodoItemUseCase(todoRepository: TodoRepository): AddTodoItemUseCase {
        return AddTodoItemUseCase(todoRepository)
    }
}

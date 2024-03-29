package com.todo.core.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.todo.common.APIConstants.BASE_URl
import com.todo.common.Constant.Companion.TODO_DATABASE_NAME
import com.todo.core.database.api.ApiInterceptor
import com.todo.core.database.api.ApiInterface
import com.todo.core.database.cached.TodoDatabase
import com.todo.core.database.cached.sharedPref.TodoSharedPrefDao
import com.todo.core.database.interfaces.ITodoDB
import com.todo.core.di.qualifier.ProcessorRoomDB
import com.todo.core.di.qualifier.ProcessorSharedPref
import com.todo.core.repositories.TodoRepositoryImpl
import com.todo.domain.interfaces.repositories.TodoRepository
import com.todo.domain.usecases.AddTodoItemUseCase
import com.todo.domain.usecases.GetTodoItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDataBase(application: Application): TodoDatabase {
        return Room.databaseBuilder(application, TodoDatabase::class.java, TODO_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    @ProcessorRoomDB
    fun provideTodoDao(todoDatabase: TodoDatabase): ITodoDB {
        return todoDatabase.getTodoDao()
    }

    @Provides
    @Singleton
    @ProcessorSharedPref
    fun provideSharedPrefTodoDao(@ApplicationContext context: Context, gson: Gson): ITodoDB {
        return TodoSharedPrefDao(context, gson)
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
    fun provideTodoRepository(@ProcessorRoomDB todoDao: ITodoDB, apiInterface: ApiInterface): TodoRepository {
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
}

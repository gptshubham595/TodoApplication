package com.example.todoapplication.di

import android.app.Application
import androidx.room.Room
import com.example.todoapplication.common.APIConstants.Companion.BASE_URl
import com.example.todoapplication.common.Constant.Companion.TODO_DATABASE_NAME
import com.example.todoapplication.data.database.api.ApiInterceptor
import com.example.todoapplication.data.database.api.ApiInterface
import com.example.todoapplication.data.database.cached.TodoDao
import com.example.todoapplication.data.database.cached.TodoDatabase
import com.example.todoapplication.data.repositories.TodoRepositoryImpl
import com.example.todoapplication.domain.interfaces.repositories.ITodoRepository
import com.example.todoapplication.domain.usecases.AddTodoItemUseCase
import com.example.todoapplication.domain.usecases.GetTodoItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
    fun provideTodoDao(todoDatabase: TodoDatabase): TodoDao {
        return todoDatabase.getTodoDao()
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
            ApiInterface::class.java,
        )
    }

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao: TodoDao, apiInterface: ApiInterface): ITodoRepository {
        return TodoRepositoryImpl(todoDao, apiInterface)
    }

    @Provides
    @Singleton
    fun provideGetTodoItemsUseCase(todoRepositoryImpl: ITodoRepository): GetTodoItemsUseCase {
        return GetTodoItemsUseCase(todoRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideAddTodoItemUseCase(todoRepositoryImpl: ITodoRepository): AddTodoItemUseCase {
        return AddTodoItemUseCase(todoRepositoryImpl)
    }

}
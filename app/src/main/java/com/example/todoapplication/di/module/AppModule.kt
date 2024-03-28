package com.example.todoapplication.di.module

import android.app.Application
import androidx.room.Room
import com.example.todoapplication.common.APIConstants.BASE_URl
import com.example.todoapplication.common.Constant.Companion.TODO_DATABASE_NAME
import com.example.todoapplication.data.database.api.ApiInterceptor
import com.example.todoapplication.data.database.api.ApiInterface
import com.example.todoapplication.data.database.cached.TodoDatabase
import com.example.todoapplication.data.database.interfaces.ITodoDB
import com.example.todoapplication.data.repositories.TodoRepositoryImpl
import com.example.todoapplication.di.qualifier.ProcessorRoomDB
import com.example.todoapplication.di.qualifier.ProcessorSharedPref
import com.example.todoapplication.domain.interfaces.repositories.ITodoRepository
import com.example.todoapplication.domain.usecases.AddTodoItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideSharedPrefTodoDao(todoDatabase: TodoDatabase): ITodoDB {
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
            ApiInterface::class.java
        )
    }

    @Provides
    @Singleton
    fun provideTodoRepository(@ProcessorRoomDB todoDao: ITodoDB, apiInterface: ApiInterface): ITodoRepository {
        return TodoRepositoryImpl(todoDao, apiInterface)
    }

//    @Provides
//    @Singleton
//    fun provideGetTodoItemsUseCase(todoRepositoryImpl: ITodoRepository): GetTodoItemsUseCase {
//        return GetTodoItemsUseCase(todoRepositoryImpl)
//    }

    @Provides
    @Singleton
    fun provideAddTodoItemUseCase(todoRepositoryImpl: ITodoRepository): AddTodoItemUseCase {
        return AddTodoItemUseCase(todoRepositoryImpl)
    }
}

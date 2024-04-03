package com.todo.core.network.api

import com.todo.common.APIConstants
import com.todo.data.models.TodoItemEntity
import retrofit2.http.GET

interface ApiService {

    @GET(APIConstants.POSTS_ENDPOINT)
    suspend fun getPosts(
//        @Header(APIConstants.TOKEN) token: String
    ): List<TodoItemEntity>
}

package com.example.todoapplication.data.database.api

import com.example.todoapplication.common.APIConstants
import com.example.todoapplication.data.models.TodoItem
import retrofit2.http.GET

interface ApiInterface {

    @GET(APIConstants.POSTS_ENDPOINT)
    suspend fun getPosts(
//        @Header(APIConstants.TOKEN) token: String
    ): List<TodoItem>
}

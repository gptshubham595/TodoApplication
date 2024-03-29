package com.todo.common

import com.google.gson.annotations.SerializedName

object APIConstants {

    const val BASE_URl = "https://jsonplaceholder.typicode.com"
    const val POSTS_ENDPOINT = "/posts"
    const val TOKEN = ""

    data class GenericResponse<T>(
        @SerializedName("status")
        val status: Long,
        @SerializedName("data")
        val data: T
    )
}

package com.example.todoapplication.common

import com.google.gson.annotations.SerializedName

class APIConstants {
    companion object {

        const val BASE_URl = ""
        const val POSTS_ENDPOINT = ""
        const val TOKEN = ""

        data class GenericResponse<T>(
            @SerializedName("status")
            val status: Long,
            @SerializedName("data")
            val data: T
        )
    }
}
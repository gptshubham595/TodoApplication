package com.example.todoapplication.common

import java.lang.Exception

class Utils {
    enum class TodoStatus {
        PENDING,
        COMPLETED
    }


    sealed class Either<out L, out R> {
        data class Error<out L>(val exception: L) : Either<L, Nothing>()
        data class Success<out R>(val data: R) : Either<Nothing, R>()
    }
}
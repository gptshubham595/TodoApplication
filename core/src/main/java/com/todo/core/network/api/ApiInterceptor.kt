package com.todo.core.network.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalReq = chain.request()
        val modifiedRequest = originalReq.newBuilder().header("token", "").build()

        val response = chain.proceed(modifiedRequest)
        return response
    }
}

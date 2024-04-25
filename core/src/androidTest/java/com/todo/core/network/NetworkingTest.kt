package com.todo.core.network

import com.todo.common.APIConstants.BASE_URl
import com.todo.core.network.api.ApiService
import java.net.HttpURLConnection
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkingTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(BASE_URl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testApiCallSuccess() {
        // Enqueue a mock response with 200 status code
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
//            .setBody("{status: 200, data: []}")
        )

        runBlocking {
            // Make an API call
            val response = runBlocking { apiService.getPosts() }
            assertEquals(HttpURLConnection.HTTP_OK, response)
        }
    }

    @Test
    fun testApiCallFailure() {
        // Enqueue a mock response with 404 status code
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND))

        runBlocking {
            // Make an API call
            val response = apiService.getPosts()

            // Assert response is not successful
            assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response)
        }
    }

    @Test
    fun testGetPosts() = runBlocking {
        // Add test case to verify the response body
        val mockResponse = MockResponse()
            .setBody(
                "[]"
            )
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getPosts()
        mockWebServer.takeRequest()

        assertEquals(true, response.isEmpty())
    }

    // Add more test cases as needed to cover different scenarios
}

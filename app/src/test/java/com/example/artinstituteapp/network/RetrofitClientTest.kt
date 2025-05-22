package com.example.artinstituteapp.network

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class RetrofitClientTest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
    
    @Test
    fun `test RetrofitClient makes correct API calls`() = runBlocking {
        // Prepare mock response
        val mockResponseJson = """
            {
                "data": {
                    "id": 1,
                    "title": "Test Artwork"
                }
            }
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(mockResponseJson))

        // Make API call using RetrofitClient
        val api = RetrofitClient.instance
        val response = api.getArtworkById(1)

        // Verify the request
        val request = mockWebServer.takeRequest()
        assert(request.method == "GET")
        assert(request.headers["Accept"]?.contains("application/json") == true)
        
        // Verify response handling
        assertNotNull(response.body())
        assertEquals(1, response.body()?.data?.id)
    }
    
    @Test
    fun `test RetrofitClient singleton behavior`() {
        val instance1 = RetrofitClient.instance
        val instance2 = RetrofitClient.instance
        
        // Verify both instances are the same object
        assert(instance1 === instance2)
    }
}

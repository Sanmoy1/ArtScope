package com.example.artinstituteapp.network

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArtInstituteApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: ArtInstituteApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtInstituteApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test searchArtworks returns correct response`() = runBlocking {
        // Prepare mock response
        val mockResponseJson = """
            {
                "data": [{
                    "id": 1,
                    "title": "Test Artwork",
                    "artist_display": "Test Artist",
                    "image_id": "test-image",
                    "date_display": "2025",
                    "medium_display": "Oil on canvas",
                    "dimensions": "100 × 100 cm",
                    "artwork_type_title": "Painting",
                    "department_title": "Modern Art",
                    "gallery_title": "Gallery 1",
                    "gallery_id": 1,
                    "credit_line": "Test Credit",
                    "place_of_origin": "Test Place",
                    "api_link": "test-link"
                }],
                "pagination": {
                    "total": 1,
                    "limit": 1,
                    "offset": 0,
                    "total_pages": 1,
                    "current_page": 1
                },
                "config": {
                    "iiif_url": "https://test.com/iiif/2",
                    "website_url": "https://test.com"
                }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponseJson))

        // Make API call
        val response = api.searchArtworks("test")

        // Verify request
        val request = mockWebServer.takeRequest()
        val expectedFields = ArtInstituteApi.FIELDS.replace(",", "%2C")
        assertEquals("/artworks/search?q=test&fields=$expectedFields&limit=15&page=1", request.path)

        // Verify response
        assertNotNull(response.body())
        val artworkResponse = response.body()!!
        assertEquals(1, artworkResponse.artworks.size)
        
        val artwork = artworkResponse.artworks[0].toArtwork()
        assertEquals(1, artwork.id)
        assertEquals("Test Artwork", artwork.title)
        assertEquals("Test Artist", artwork.artistDisplay)
    }

    @Test
    fun `test getArtworkById returns correct response`() = runBlocking {
        // Prepare mock response
        val mockResponseJson = """
            {
                "data": {
                    "id": 1,
                    "title": "Test Artwork",
                    "artist_display": "Test Artist",
                    "image_id": "test-image",
                    "date_display": "2025",
                    "medium_display": "Oil on canvas",
                    "dimensions": "100 × 100 cm",
                    "artwork_type_title": "Painting",
                    "department_title": "Modern Art",
                    "gallery_title": "Gallery 1",
                    "gallery_id": 1,
                    "credit_line": "Test Credit",
                    "place_of_origin": "Test Place",
                    "api_link": "test-link"
                }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponseJson))

        // Make API call
        val response = api.getArtworkById(1)

        // Verify request
        val request = mockWebServer.takeRequest()
        val expectedFields = ArtInstituteApi.FIELDS.replace(",", "%2C")
        assertEquals("/artworks/1?fields=$expectedFields", request.path)

        // Verify response
        assertNotNull(response.body())
        val artworkDto = response.body()!!.data
        val artwork = artworkDto.toArtwork()
        
        assertEquals(1, artwork.id)
        assertEquals("Test Artwork", artwork.title)
        assertEquals("Test Artist", artwork.artistDisplay)
    }

    @Test
    fun `test searchArtworks handles error response`() = runBlocking {
        // Prepare error response
        val errorResponse = MockResponse()
            .setResponseCode(500)
            .setBody("{\"error\": \"Internal Server Error\"}")
        mockWebServer.enqueue(errorResponse)

        // Make API call
        val response = api.searchArtworks("test")

        // Verify response
        assertEquals(500, response.code())
        assertEquals(false, response.isSuccessful)
    }

    @Test
    fun `test getArtworkById handles error response`() = runBlocking {
        // Prepare error response
        val errorResponse = MockResponse()
            .setResponseCode(404)
            .setBody("{\"error\": \"Artwork not found\"}")
        mockWebServer.enqueue(errorResponse)

        // Make API call
        val response = api.getArtworkById(999999)

        // Verify response
        assertEquals(404, response.code())
        assertEquals(false, response.isSuccessful)
    }
}

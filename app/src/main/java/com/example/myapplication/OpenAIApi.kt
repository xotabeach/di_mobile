package com.example.myapplication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

data class OpenAIRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val text: String
)

interface OpenAIApi {
    @POST("v1/completions")
    suspend fun generateDiet(
        @Header("Authorization") apiKey: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse

    companion object {
        fun create(): OpenAIApi {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenAIApi::class.java)
        }
    }
}

package com.vikas.posedetection.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    companion object {
        private const val baseUrl = "https://poseapi.letscodeon.in"
        fun buildApi(authToken: String): ApiService {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor { chain ->
                            chain.proceed(chain.request().newBuilder().also {
                                it.addHeader("Authorization", "Bearer $authToken")
                            }.build())
                        }
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
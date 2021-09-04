package com.vikas.posedetection.data.network

import com.vikas.posedetection.data.model.ContentResponse
import com.vikas.posedetection.data.model.LoginResponse
import com.vikas.posedetection.data.model.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<LoginResponse>

    @GET("auth/uploads")
    fun getUploads(@Query("userId") userId: Int): Call<ContentResponse>

    @Multipart
    @POST("upload/image")
    fun uploadResult(@Query("userId") userId: Int, @Part image: MultipartBody.Part, @Part("data") data: RequestBody): Call<Response>
}
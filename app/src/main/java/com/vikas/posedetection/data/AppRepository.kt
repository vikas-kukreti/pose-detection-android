package com.vikas.posedetection.data

import android.R.attr
import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.mlkit.vision.pose.Pose
import com.vikas.posedetection.data.model.ContentResponse
import com.vikas.posedetection.data.model.LoginResponse
import com.vikas.posedetection.data.model.User
import com.vikas.posedetection.data.network.ApiService
import com.vikas.posedetection.data.network.RemoteDataSource
import com.vikas.posedetection.data.preferences.UserPreferences
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.R.attr.bitmap
import java.io.FileOutputStream
import java.lang.Exception


class AppRepository {
    private lateinit var api: ApiService
    private lateinit var preferences: UserPreferences

    val poseBitmap = MutableLiveData<Bitmap>()
    val pose = MutableLiveData<Pose>()

    val uploading = MutableLiveData(false)
    val uploads = MutableLiveData<ContentResponse>(null)

    fun setPoseData(pose: Pose) {
        this.pose.value = pose
    }
    fun setPoseBitmap(poseBitmap: Bitmap) {
        this.poseBitmap.value = poseBitmap
    }
    companion object {
        private lateinit var appRepository: AppRepository
        fun initialize(context: Context) {
            appRepository = AppRepository()
            appRepository.preferences = UserPreferences(context)
            appRepository.api = RemoteDataSource.buildApi(appRepository.preferences.get("token"))
        }
        fun get(): AppRepository {
            return appRepository
        }
    }


    fun login(email: String, password: String, callback: (result: LoginResponse) -> Unit) {
        api.login(email, password)
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    callback.invoke(response.body()!!)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    callback.invoke(LoginResponse(0, "Something went wrong!", "", null))
                }

            })
    }

    fun saveLoginInfo(token: String?, user: User?) {
        if (token != null) {
            preferences.put("token", token)
        }
        if(user != null) {
            preferences.put("user", Gson().toJson(user, User::class.java))
        }
    }

    fun isLoggedIn(): Boolean {
        return preferences.get("token") != ""
    }

    fun getAuthToken(): String {
        return preferences.get("token")
    }

    fun getUserId(): Int {

        val user = Gson().fromJson(preferences.get("user"), User::class.java)
        return user.id

    }

    fun getUserInfo(): User {
        return Gson().fromJson(preferences.get("user"), User::class.java)
    }

    fun getContent() {
        api.getUploads(getUserId())
            .enqueue(object: Callback<ContentResponse> {
                override fun onResponse(
                    call: Call<ContentResponse>,
                    response: Response<ContentResponse>
                ) {
                    uploads.value = response.body()
                }

                override fun onFailure(call: Call<ContentResponse>, t: Throwable) {
                    uploads.value = ContentResponse(0, "Something went wrong!", listOf(), listOf(), listOf())
                }

            })

    }

    fun uploadPoseData(
        context: Context,
        image: Bitmap,
        data: String,
        callback: (result: com.vikas.posedetection.data.model.Response) -> Unit
    ) {
        val dir = File(context.cacheDir, "images")
        dir.mkdirs()
        val file = File.createTempFile("image_", ".jpg", dir)
        try {
            val out = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val dataBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), data)
            api.uploadResult(getUserId(), body, dataBody).enqueue(object: Callback<com.vikas.posedetection.data.model.Response> {
                override fun onResponse(
                    call: Call<com.vikas.posedetection.data.model.Response>,
                    response: Response<com.vikas.posedetection.data.model.Response>
                ) {
                    response.body()?.let { callback.invoke(it) }
                }

                override fun onFailure(
                    call: Call<com.vikas.posedetection.data.model.Response>,
                    t: Throwable
                ) {
                    callback.invoke(com.vikas.posedetection.data.model.Response(0, "Something went wrong!"))
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            callback.invoke(com.vikas.posedetection.data.model.Response(0, "Something went wrong!"))
        }


    }

    fun logout() {
        preferences.clear()
    }
}
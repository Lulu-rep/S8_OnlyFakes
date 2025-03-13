package fr.isen.onlyfakes.services.pictshare

import fr.isen.onlyfakes.models.UploadResponsePost
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PictshareApi {
    @Multipart
    @POST("/api/upload.php") // Replace with your actual endpoint
    fun uploadImage(@Part file: MultipartBody.Part): Call<UploadResponsePost>
}


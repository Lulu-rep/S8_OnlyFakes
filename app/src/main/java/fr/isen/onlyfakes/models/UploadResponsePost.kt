package fr.isen.onlyfakes.models

import com.google.gson.annotations.SerializedName

data class UploadResponsePost(
    @SerializedName("status") val status: String,
    @SerializedName("hash") val hash: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("reason") val reason: String?
)


import fr.isen.onlyfakes.services.pictshare.PictshareApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object RetrofitInstance {
    private const val BASE_URL = "http://129.151.236.131/api/upload.php/"

    private val client = OkHttpClient.Builder().build()

    val api: PictshareApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PictshareApi::class.java)
    }
}
package nullex.updater.fetchModelName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
data class DeviceInfo(
    val brand: String,
    val name: String
)
object ModelNameRetro {
    private const val BASE_URL = "https://raw.githubusercontent.com/bsthen/device-models/refs/heads/main/"
    val modelNameGitContent: ModelNameInterface by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ModelNameInterface::class.java)
    }
}
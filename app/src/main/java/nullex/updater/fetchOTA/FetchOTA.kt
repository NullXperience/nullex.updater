package nullex.updater.fetchOTA
import retrofit2.Retrofit
object FetchOTA {
    private const val BASE_URL = "https://raw.githubusercontent.com/NullXperience/json_ota/refs/heads/main/"
    val getOTA: FetchOTAInterface by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).build().create(FetchOTAInterface::class.java)
    }
}
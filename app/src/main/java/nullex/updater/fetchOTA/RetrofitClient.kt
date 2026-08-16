package nullex.updater.fetchOTA
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
data class OtaResponse (
    val models: Map<String, OtaModel>,
    val supported: String
)
data class OtaModel (
    val changelogs: Map<String, ChangelogReference>,
    val version: String,
    val url: String,
    val sha256: String,
    val size: Long,
    val isMB: Boolean,
    val isIncremental: Boolean,
    val isFull: Boolean
)
data class ChangelogReference (
    val changelogHeader: String,
    val changelogs: String
)
object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/NullXperience/json_ota/refs/heads/main/"
    val githubUserContent: RetrofitInterface by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitInterface::class.java)
    }
}
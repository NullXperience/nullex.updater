package nullex.updater.fetch.api
import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
data class DeviceInfo(
    val brand: String,
    val name: String
)
data class OtaResponse (
    val models: Map<String, OtaModel>,
    val supported: String
)
data class OtaModel (
    val changelogs: Map<String, ChangelogReference>,
    val version: String
)
data class ChangelogReference (
    val changelogHeader: String,
    val changelogs: String,
    val buildid: String,
    val isIncremental: Boolean,
    val isFull: Boolean,
    val url: String,
    val sha256: String,
    val size: String,
)
object ClientManager {
    private const val BASE_URL = "https://raw.githubusercontent.com/";
    private const val MODEL_URL = "https://raw.githubusercontent.com/bsthen/device-models/refs/heads/main/devices.json";
    private const val OTA_URL = "https://github.com/NullXperience/json_ota/releases/download/test/ota.zip";
    private const val METADATA_URL = "https://raw.githubusercontent.com/NullXperience/json_ota/refs/heads/main/ota.json";
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val api: ClientInterface by lazy {
        retrofit.create(ClientInterface::class.java)
    }
    suspend fun getDevices(): Map<String, DeviceInfo>
    {
        return api.getDevices(MODEL_URL)
    }
    suspend fun getOtaInfo(): OtaResponse
    {
        return api.getOtaInfo(METADATA_URL)
    }
    suspend fun downloadOTA(context: Context, deviceCodename: String, isIncremental: Boolean): File
    {
        val fileName = if(!isIncremental) "$deviceCodename-ota.zip" else "$deviceCodename-incremental.zip"
        val responseBody = api.getOTAPackage(OTA_URL)
        val file = File(context.getExternalFilesDir(null), fileName);
        responseBody.byteStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output);
            }
        }
        return file;
    }
}
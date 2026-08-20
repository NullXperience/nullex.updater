package nullex.updater.fetchOTA
import retrofit2.Retrofit
import android.os.Environment
import java.io.File
object FetchOTA {
    private const val BASE_URL = "https://github.com/NullXperience/json_ota/releases/tag/test/"
    suspend fun downloadOTA(): File
    {
        val responseBody = getOTA.getOTAPackage();
        val file = File(Environment.getExternalStorageDirectory().path, "ota.zip");
        responseBody.byteStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output);
            }
        }
        return file;
    }
    val getOTA: FetchOTAInterface by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).build().create(FetchOTAInterface::class.java);
    }
}
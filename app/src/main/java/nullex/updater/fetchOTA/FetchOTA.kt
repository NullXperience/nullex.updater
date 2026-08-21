package nullex.updater.fetchOTA
import android.content.Context
import android.widget.Toast
import retrofit2.Retrofit
import java.io.File
object FetchOTA {
    private const val BASE_URL = "https://github.com/NullXperience/json_ota/releases/tag/test/"
    suspend fun downloadOTA(context: Context, deviceCodename: String, isIncremental: Boolean): File
    {
        val fileName = if(!isIncremental) "${deviceCodename}-ota.zip" else "${deviceCodename}-incremental.zip";
        Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show()
        val responseBody = getOTA.getOTAPackage();
        val file = File(context.getExternalFilesDir(null)?.path, fileName);
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
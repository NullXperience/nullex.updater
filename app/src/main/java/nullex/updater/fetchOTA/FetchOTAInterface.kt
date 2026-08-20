package nullex.updater.fetchOTA
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
interface FetchOTAInterface {
    @Streaming
    @GET("ota.zip")
    suspend fun getOTAPackage(): ResponseBody
}
package nullex.updater.fetchOTA
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
interface FetchOTAInterface {
    @Streaming
    @GET("ota/update.zip")
    suspend fun getOTAPackage(@Url url: String): ResponseBody
}
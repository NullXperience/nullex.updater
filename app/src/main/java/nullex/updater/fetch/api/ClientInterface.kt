package nullex.updater.fetch.api
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
interface ClientInterface {
    @GET
    suspend fun getDevices(@Url url: String): Map<String, DeviceInfo>
    @GET
    suspend fun getOtaInfo(@Url url: String): OtaResponse
    @Streaming
    @GET
    suspend fun getOTAPackage(@Url url: String): ResponseBody
}
package nullex.updater.fetchOTAMetadata
import retrofit2.http.GET
interface RetrofitInterface {
    @GET("ota.json")
    suspend fun getOtaInfo(): OtaResponse;
}
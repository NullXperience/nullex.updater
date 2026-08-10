package nullex.updater.fetchOTA
import retrofit2.http.GET
interface RetrofitInterface {
    @GET("ota.json")
    suspend fun getOtaInfo(): OtaResponse;
}
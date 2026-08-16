package nullex.updater.fetchModelName
import retrofit2.http.GET
interface ModelNameInterface {
    @GET("devices.json")
    suspend fun getDevices(): Map<String, DeviceInfo>
}
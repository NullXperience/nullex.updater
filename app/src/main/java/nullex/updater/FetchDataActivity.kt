package nullex.updater
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nullex.updater.fetchOTA.OtaModel
import nullex.updater.fetchOTA.RetrofitClient
class FetchDataActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null)
        {
            supportFragmentManager.beginTransaction().replace(R.id.ThisFullscreenFragment, MainScreenFragment()).commit();
        };
    }
    object OtaData {
        var preferredModel: OtaModel? = null
            private set;
        suspend fun load()
        {
            val response = RetrofitClient.githubUserContent.getOtaInfo();
            preferredModel = response.models["device_one"];
        }
    }
}
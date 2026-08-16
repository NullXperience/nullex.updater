package nullex.updater
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import nullex.updater.fetchOTA.OtaModel
import nullex.updater.fetchOTA.RetrofitClient
import nullex.updater.fetchModelName.ModelNameRetro
class MainScreenFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_screen, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        val ovrTXT: TextView = view.findViewById(R.id.overlayTextView);
        if(isInternetAvailable())
        {
            viewLifecycleOwner.lifecycleScope.launch {
                OtaData.load();
                OtaData.getModel();
                view.findViewById<TextView>(R.id.model).text = OtaData.deviceName;
                if(OtaData.isSupported)
                {
                    try {
                        val ver = OtaData.getVersion();
                        if(OtaData.preferredModel!!.version == ver)
                        {
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.ThisFragmentContainer, NotFound()).commit();
                        }
                    }
                    catch(e: Exception)
                    {
                        ovrTXT.text = getString(R.string.unknown);
                    }
                }
                else ovrTXT.text = getString(R.string.tampered_supported);
            }
        }
        else
        {
            ovrTXT.visibility = View.GONE;
            view.findViewById<TextView>(R.id.no_internet_text).visibility = View.VISIBLE;
        }
    }
    fun isInternetAvailable(): Boolean
    {
        val connectivityManager = requireActivity().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager;
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork);
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true;
    }
    object OtaData {
        lateinit var deviceName: String;
        var preferredModel: OtaModel? = null
            private set;
        var isSupported: Boolean = false;
        suspend fun load()
        {
            val response = RetrofitClient.githubUserContent.getOtaInfo();
            preferredModel = response.models[Build.MODEL];
            val supportedDevices = response.supported.split(",").map { it.trim() }
            isSupported = Build.MODEL.trim() in supportedDevices;
        }
        suspend fun getModel()
        {
            val response = ModelNameRetro.modelNameGitContent.getDevices();
            deviceName = response[Build.MODEL]?.name ?: Build.MODEL;
        }
        fun getVersion(): String = Build.DISPLAY.split(" ").getOrNull(1) ?: "null";
    }
}
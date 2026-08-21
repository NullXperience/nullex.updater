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
import nullex.updater.fetchModelName.ModelNameRetro
import nullex.updater.fetchOTAMetadata.OtaModel
import nullex.updater.fetchOTAMetadata.RetrofitClient
import nullex.updater.fetchOTAMetadata.ChangelogReference
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
        if(!isInternetAvailable())
        {
            ovrTXT.visibility = View.GONE;
            view.findViewById<TextView>(R.id.no_internet_text).visibility = View.VISIBLE;
        }
        else viewLifecycleOwner.lifecycleScope.launch {
            OtaMetadata.load();
            view.findViewById<TextView>(R.id.model).text = OtaMetadata.deviceName;
            if(!OtaMetadata.isSupported || OtaMetadata.preferredModel?.version == OtaMetadata.currentSystemVersion)
            {
                if(OtaMetadata.isSupported)
                {
                    ovrTXT.text = getString(R.string.not_found);
                    parentFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.ThisFragmentContainer, NotFound()).commit();
                }
                else ovrTXT.text = getString(R.string.unknown);
            }
            else if(OtaMetadata.preferredModel?.version != OtaMetadata.currentSystemVersion)
            {
                ovrTXT.text = getString(R.string.found);
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                    .replace(R.id.ThisFragmentContainer, UpdatesAvailable()).commit();
            }
        }
    }
    fun isInternetAvailable(): Boolean
    {
        val connectivityManager = requireActivity().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager;
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork);
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true;
    }
    object OtaMetadata {
        lateinit var actualDeviceName: String;
        var deviceName: String? = null
            private set
        var buildID: String? = null
            private set
        var OTAUrl: String? = null
            private set
        var SHA256: String? = null
            private set
        var size: String? = null
            private set
        var latestVersion: String? = null
            private set
        var preferredModel: OtaModel? = null
            private set
        var versionSpecific: ChangelogReference? = null
            private set
        var isIncremental: Boolean = false
            private set
        var isSupported: Boolean = true
            private set
        var expandVersionInfo: Boolean = true
        var currentSystemVersion: String? = null
            private set;
        suspend fun load()
        {
            val metadata = RetrofitClient.githubUserContent.getOtaInfo();
            val deviceModel = ModelNameRetro.modelNameGitContent.getDevices();
            //init
            currentSystemVersion = Build.DISPLAY.split(" ").getOrNull(1) ?: "1.0.0";
            deviceName = deviceModel[Build.MODEL]?.name ?: Build.MODEL;
            //actualDeviceName = Build.MODEL.toString();
            actualDeviceName = "device_one"
            // isSupported = deviceName?.let { name -> metadata.supported.split(",").any { it.trim().equals(name.trim(), ignoreCase = true) } } == true;
            if(isSupported)
            {
                preferredModel = metadata.models["device_one"];
                latestVersion = preferredModel!!.version;
                versionSpecific = preferredModel!!.changelogs[preferredModel!!.version];
                OTAUrl = versionSpecific!!.url;
                SHA256 = versionSpecific!!.sha256;
                size = versionSpecific!!.size;
                buildID = versionSpecific!!.buildid;
                isIncremental = versionSpecific!!.isIncremental;
            }
        }
    }
}
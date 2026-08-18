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
import android.widget.Toast
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
        if(isInternetAvailable())
        {
            viewLifecycleOwner.lifecycleScope.launch {
                OtaMetadata.load();
                view.findViewById<TextView>(R.id.model).text = OtaMetadata.deviceName;
                if(OtaMetadata.isSupported)
                {
                    try {
                        if(OtaMetadata.preferredModel!!.version == OtaMetadata.currentSystemVersion)
                        {
                            ovrTXT.text = getString(R.string.not_found);
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.ThisFragmentContainer, NotFound()).commit();
                        }
                        else if(OtaMetadata.preferredModel!!.version != OtaMetadata.currentSystemVersion)
                        {
                            ovrTXT.text = getString(R.string.found);
                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.ThisFragmentContainer, UpdatesAvailable()).commit();
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
    object OtaMetadata {
        var deviceName: String = Build.MODEL
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
        var isSupported: Boolean = false
            private set
        var isIncremental: Boolean = false
            private set
        var expandVersionInfo: Boolean = true
        val currentSystemVersion: String =
            Build.DISPLAY.split(" ").getOrNull(1) ?: "1.0.0"
        suspend fun load() {
            val metadata = RetrofitClient.githubUserContent.getOtaInfo();
            val devices = ModelNameRetro.modelNameGitContent.getDevices();
            deviceName = devices[Build.MODEL]?.name ?: Build.MODEL;
            isSupported = Build.MODEL.trim() in metadata.supported;
            preferredModel = metadata.models[deviceName];
            val model = preferredModel ?: return;
            latestVersion = model.version;
            versionSpecific = model.changelogs[model.version];
            val version = versionSpecific ?: return;
            OTAUrl = version.url;
            SHA256 = version.sha256;
            size = version.size;
            buildID = version.buildid;
            isIncremental = version.isIncremental;
        }
    }
}
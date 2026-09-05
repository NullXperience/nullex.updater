package nullex.updater
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nullex.updater.fetch.api.ChangelogReference
import nullex.updater.fetch.api.ClientManager
import nullex.updater.fetch.api.OtaModel
import kotlin.time.Duration.Companion.milliseconds
import android.icu.util.Calendar
import androidx.core.content.edit
class MainScreenFragment : Fragment()
{
    // button init:
    private lateinit var statLabel: TextView;
    private lateinit var icStat: ImageView;
    private lateinit var curDevice: TextView;
    private lateinit var codenameOfDevice: TextView;
    private lateinit var checkingSpinner: ProgressBar;
    private lateinit var overlayTextView: TextView;
    private lateinit var changelogText: TextView;
    private lateinit var checkOta: FrameLayout;
    private lateinit var checkOtaText: TextView;
    private lateinit var updateProgressBar: ProgressBar;
    private lateinit var divider: View;
    private lateinit var lastUpdated: TextView;
    private lateinit var updateProgressBarText: TextView;
    private lateinit var creditsWindow: FrameLayout;
    private lateinit var calendarData: Calendar;
    private lateinit var sharedPreferences: SharedPreferences;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_screen, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        // yooo
        statLabel = view.findViewById(R.id.statusLabel); // the one in the top right corner
        icStat = view.findViewById(R.id.ic_stat); // The one before statLabel
        curDevice = view.findViewById(R.id.currentDevice); // A text that is used to show the device model - inside the box
        codenameOfDevice = view.findViewById(R.id.codename); // codename, below the one above.
        checkingSpinner = view.findViewById(R.id.checkingSpinner); // djfoijoj iefojfpjfpjfpjfjfpef im verity im obesity im a j*bless entity
        overlayTextView = view.findViewById(R.id.overlayTextView); // checking texxxxxxxxxxxxxxxxx
        changelogText = view.findViewById(R.id.changelogText); // checking texxxxxxxxxxxxxxxxx
        checkOta = view.findViewById(R.id.checkForUpdates); // otaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        checkOtaText = view.findViewById(R.id.checkForUpdatesText); // otaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        updateProgressBar = view.findViewById(R.id.updateProgressBar); /// update progress bar
        divider = view.findViewById(R.id.dividerShit); /// the DIVIIVDJKNUF<K
        lastUpdated = view.findViewById(R.id.lastchecked);
        updateProgressBarText = view.findViewById(R.id.updateProgressBarText);
        creditsWindow = view.findViewById(R.id.credits);
        calendarData = Calendar.getInstance();
        sharedPreferences = requireContext().getSharedPreferences(UPDATER_PREFERENCES, MODE_PRIVATE);
        // it starts with ONE THING IDK WHY IT DOESN'T EVEN MATTER HOW HARD YOU TRY
        // xaxaxaxaxaxxaxa let's start and btw let's hide some of these stuff for now
        codenameOfDevice.text = Build.MODEL;
        setElementState(View.GONE, divider);
        if(!isInternetAvailable())
        {
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.nointernet))
                .setNegativeButton(getString(android.R.string.ok)) { _, which -> requireActivity().finish(); }.setCancelable(false).show()
        }
        // let's check if we have the model name inside sharedPreferences and if not, let's just get it from the cloud and stop fetching it afterwards.
        if(sharedPreferences.getString(MODEL_NAME, null) == null)
        {
            val diagView = layoutInflater.inflate(R.layout.loading_material, null);
            diagView.findViewById<TextView>(R.id.loadingMessage).text = getString(R.string.fetching_info, "device model data");
            val dialog = MaterialAlertDialogBuilder(requireContext()).setView(diagView).setCancelable(false).create();
            viewLifecycleOwner.lifecycleScope.launch {
                dialog.show();
                OtaMetadata.deviceModel = ClientManager.getDevices()[Build.MODEL]?.name ?: Build.MODEL;
                curDevice.text = getString(R.string.brand_plus_model, Build.MANUFACTURER, OtaMetadata.deviceModel);
                dialog.dismiss();
                sharedPreferences.edit {
                    putString(MODEL_NAME, OtaMetadata.deviceModel);
                }
            }
        }
        else curDevice.text = getString(R.string.brand_plus_model, Build.MANUFACTURER, sharedPreferences.getString(MODEL_NAME, null));
        checkOta.setOnClickListener { view ->
            setElementState(View.VISIBLE, divider);
            lastUpdated.text = getString(R.string.last_checked, calendarData.get(Calendar.DATE).toString(), calendarData.get(Calendar.MONTH).toString(), calendarData.get(Calendar.YEAR).toString());
            viewLifecycleOwner.lifecycleScope.launch {
                OtaMetadata.load();
                delay(3000L.milliseconds);
                if(OtaMetadata.preferredModel?.version == OtaMetadata.currentSystemVersion)
                {
                    checkingSpinner.visibility = View.GONE;
                    overlayTextView.text = getString(R.string.lastest_ver);
                    lastUpdated.visibility = View.VISIBLE;
                }
                else if(OtaMetadata.preferredModel?.version != OtaMetadata.currentSystemVersion)
                {
                    checkOtaText.text = getString(R.string.download);
                    icStat.setImageResource(R.drawable.ic_status_dot_red);
                    statLabel.text = getString(R.string.status_old_to_date);
                    statLabel.setTextColor(MaterialColors.getColor(statLabel, R.attr.otaWarning));
                    setElementState(View.VISIBLE, divider);
                    checkingSpinner.visibility = View.GONE;
                    changelogText.visibility = View.VISIBLE;
                    overlayTextView.text = getString(R.string.change);
                    // handle downloads here:
                    view.setOnClickListener {
                        checkOtaText.visibility = View.GONE;
                        updateProgressBar.visibility = View.VISIBLE;
                        updateProgressBarText.visibility = View.VISIBLE;
                        updateProgressBar.progress = 50;
                        updateProgressBarText.text = getString(R.string.downloading, "50%");
                        val text = resources.getStringArray(R.array.randText);
                        val idx = (0..5).random()
                        Toast.makeText(context, text[idx], Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        creditsWindow.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).replace(R.id.ThisFullscreenFragment, CreditsFragment()).addToBackStack(null).commit();
        }
    }
    fun setElementState(state: Int)
    {
        checkingSpinner.visibility = state;
        overlayTextView.visibility = state;
    }
    fun setElementState(state: Int, addView: View)
    {
        checkingSpinner.visibility = state;
        overlayTextView.visibility = state;
        addView.visibility = state;
    }
    fun isInternetAvailable(): Boolean
    {
        val connectivityManager = requireActivity().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager;
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork);
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true;
    }
    object OtaMetadata {
        lateinit var actualDeviceName: String;
        var deviceModel: String? = null;
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
        var currentSystemVersion: String? = null
            private set;
        suspend fun load()
        {
            val metadata = ClientManager.getOtaInfo();
            //init
            currentSystemVersion = "1.0.0";
            actualDeviceName = "device_one"
            isSupported = actualDeviceName.let { name -> metadata.supported.split(",").any { it.trim().equals(name.trim(), ignoreCase = true) } } == true;
            if(isSupported)
            {
                preferredModel = metadata.models[actualDeviceName];
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
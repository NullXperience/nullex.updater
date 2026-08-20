package nullex.updater
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
class ShowChangelogs : Fragment() {
    private lateinit var msr: String;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.changelogs_window, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        val metadata = MainScreenFragment.OtaMetadata;
        val curVersion: TextView = view.findViewById(R.id.currentVersion);
        val chgTxt: TextView = view.findViewById(R.id.changelogText)
        val sv: LinearLayout = view.findViewById(R.id.softverview);
        // before we do anything, let's hide/unhide sv and also, change the version target to the latest.
        if(!metadata.expandVersionInfo)
        {
            sv.visibility = View.GONE;
            msr = metadata.latestVersion.toString();
        }
        else msr = metadata.currentSystemVersion.toString();
        curVersion.text = msr;
        lifecycleScope.launch {
            chgTxt.text = HtmlCompat.fromHtml(metadata.preferredModel!!.changelogs[msr]!!.changelogs, HtmlCompat.FROM_HTML_MODE_LEGACY);
        }
    }
}
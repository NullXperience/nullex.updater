package nullex.updater
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
class ShowChangelogs : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.changelogs_window, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        val curVersion: TextView = view.findViewById(R.id.currentVersion);
        val chgHead: TextView = view.findViewById(R.id.changelogHeader);
        val chgTxt: TextView = view.findViewById(R.id.changelogText)
        val msr: String = MainScreenFragment.OtaData.getVersion();
        curVersion.text = msr;
        lifecycleScope.launch {
            chgHead.text = MainScreenFragment.OtaData.preferredModel!!.changelogs[msr]!!.changelogHeader;
            chgTxt.text = HtmlCompat.fromHtml(MainScreenFragment.OtaData.preferredModel!!.changelogs[msr]!!.changelogs, HtmlCompat.FROM_HTML_MODE_LEGACY);
        }
    }
}
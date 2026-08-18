package nullex.updater
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
class UpdatesAvailable : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.update_available, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        // init lol
        val metadata = MainScreenFragment.OtaMetadata;
        val downloadButton: FrameLayout = view.findViewById(R.id.downloadButton);
        val verSize: TextView = view.findViewById(R.id.buildidwithsize);
        val changelogText: TextView = view.findViewById(R.id.changelogText);
        val changelogsAct: LinearLayout = view.findViewById(R.id.changelogsAction);
        // lets uhrm- idk i just wanted to write some comment so..
        metadata.expandVersionInfo = false;
        verSize.text = getString(R.string.versionAndSize, metadata.buildID, metadata.size);
        changelogText.text = HtmlCompat.fromHtml(metadata.versionSpecific!!.changelogs, HtmlCompat.FROM_HTML_MODE_LEGACY);
        changelogsAct.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.pull_up_from_bottom,R.anim.none,R.anim.pop_enter, R.anim.push_out_to_bottom)
                .addToBackStack(null).replace(R.id.ThisFullscreenFragment, ShowChangelogs()).commit();
        }
    }
}
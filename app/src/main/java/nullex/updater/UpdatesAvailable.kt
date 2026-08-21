package nullex.updater
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.launch
import nullex.updater.fetchOTA.FetchOTA

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
        val downloadButtonText: TextView = view.findViewById(R.id.downloadButtonText);
        val verSize: TextView = view.findViewById(R.id.buildidwithsize);
        val changelogText: TextView = view.findViewById(R.id.changelogText);
        val changelogsAct: LinearLayout = view.findViewById(R.id.changelogsAction);
        val bannerColor = MaterialColors.getColor(changelogsAct, R.attr.interactiveCard);
        downloadButton.backgroundTintList = ColorStateList.valueOf(bannerColor);
        val buttonTextColor = if (ColorUtils.calculateContrast(Color.WHITE, bannerColor) >=
            ColorUtils.calculateContrast(Color.BLACK, bannerColor)) Color.WHITE else Color.BLACK;
        downloadButtonText.setTextColor(buttonTextColor);
        // lets uhrm- idk i just wanted to write some comment so..
        metadata.expandVersionInfo = false;
        verSize.text = getString(R.string.versionAndSize, metadata.buildID, metadata.size);
        changelogText.text = HtmlCompat.fromHtml(metadata.versionSpecific!!.changelogs, HtmlCompat.FROM_HTML_MODE_LEGACY);
        changelogsAct.setOnClickListener {
            if(resources.configuration.smallestScreenWidthDp < 600)
            {
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.pull_up_from_bottom,R.anim.none,R.anim.pop_enter, R.anim.push_out_to_bottom)
                    .addToBackStack(null).replace(R.id.ThisFullscreenFragment, ShowChangelogs()).commit();
            }
        }
        downloadButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                try
                {
                    FetchOTA.downloadOTA()
                }
                catch(e: Exception)
                {
                    Toast.makeText(context, "Download failed: $e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
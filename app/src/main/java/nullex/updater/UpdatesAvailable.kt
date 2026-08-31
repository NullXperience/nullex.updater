package nullex.updater
import android.os.Bundle
// import android.os.RecoverySystem TODO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
class UpdatesAvailable
{
    val placebo = 1;
    /*
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
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar);
        downloadButton.backgroundTintList = ColorStateList.valueOf(bannerColor);
        val buttonTextColor = if (ColorUtils.calculateContrast(Color.WHITE, bannerColor) >=
            ColorUtils.calculateContrast(Color.BLACK, bannerColor)) Color.WHITE else Color.BLACK;
        downloadButtonText.setTextColor(buttonTextColor);
        // lets uhrm- idk i just wanted to write some comment so..
        progressBar.progress = 0;
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
                try {
                    downloadButtonText.text = getString(R.string.downloading);
                    val otaFile = ClientManager.downloadOTA(requireContext(), metadata.actualDeviceName, metadata.isIncremental, progressBar);
                    downloadButtonText.text = getString(R.string.tap2Install);
                    TODO("implement installer");
                }
                catch(e: Exception)
                {
                    Toast.makeText(context, "Download failed: $e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
     */
}
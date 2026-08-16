package nullex.updater
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
class NotFound : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.not_found, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        val currentVersion: TextView = view.findViewById(R.id.currentVersion);
        val statusText: TextView = requireActivity().findViewById(R.id.overlayTextView);
        val changelog: TextView = view.findViewById(R.id.lastChangelog);
        currentVersion.text = Build.ID;
        statusText.text = getString(R.string.not_found);
        changelog.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.ThisFullscreenFragment, ShowChangelogs())
                .addToBackStack(null)
                .commit()
        }
    }
}
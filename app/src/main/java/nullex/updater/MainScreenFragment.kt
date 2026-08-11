package nullex.updater
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainScreenFragment : Fragment()
{
    private val currentVersion: String = "1.0.1"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_screen, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        lifecycleScope.launch {
            val otaResData: FetchDataActivity.OtaData = FetchDataActivity.OtaData;
            otaResData.load();
            val lastestVersion = otaResData.preferredModel?.version;
            if(lastestVersion == currentVersion) parentFragmentManager.beginTransaction().replace(R.id.ThisFragmentContainer,
                NotFound()).commit();
        }
    }
}
package nullex.updater
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
class MainScreenActivity : AppCompatActivity()
{
    private var splitScreenPromptShown = false
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splitScreenPromptShown = savedInstanceState?.getBoolean("splitScreenPromptShown") ?: false
        if(savedInstanceState == null) supportFragmentManager.beginTransaction()
            .replace(R.id.ThisFullscreenFragment, MainScreenFragment()).commit();
    }
    override fun onResume()
    {
        super.onResume()
        if(!splitScreenPromptShown && isInMultiWindowMode)
        {
            splitScreenPromptShown = true
            MaterialAlertDialogBuilder(this)
                .setTitle("Updater")
                .setMessage(R.string.multi_window_warn)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .show()
        }
    }
    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("splitScreenPromptShown", splitScreenPromptShown);
    }
}
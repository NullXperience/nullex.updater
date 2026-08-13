package nullex.updater
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
class FetchDataActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) supportFragmentManager.beginTransaction()
            .replace(R.id.ThisFullscreenFragment, MainScreenFragment()).commit();
    }
}
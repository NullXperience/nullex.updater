package nullex.updater
class ShowChangelogs
{
    val placebooooo = 1;
    /*
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
    */
}
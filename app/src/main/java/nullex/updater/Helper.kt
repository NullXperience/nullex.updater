package nullex.updater
import java.io.File
enum class LogType()
{
    LOG_LEVEL_INFO(),
    LOG_LEVEL_WARN(),
    LOG_LEVEL_DEBUG(),
    LOG_LEVEL_ERROR(),
    LOG_LEVEL_ABORT(),
    LOG_LEVEL_STDERR();
}
class Helper
{
    private val externPath = "/storage/emulated/0/Android/data/nullex.updater/files/"
    private var thisCallFailed: Boolean = false;
    fun consoleLog(logg: LogType, service: String, message: String) {
        val theLogFile = File(externPath, "logs");
        try
        {
            when(logg)
            {
                LogType.LOG_LEVEL_INFO -> {
                    theLogFile.appendText("INFO: ${service}(): $message\n");
                }
                LogType.LOG_LEVEL_WARN -> {
                    theLogFile.appendText("WARN: ${service}(): $message\n");
                }
                LogType.LOG_LEVEL_DEBUG -> {
                    theLogFile.appendText("DEBUG: ${service}(): $message\n");
                }
                LogType.LOG_LEVEL_ERROR -> {
                    theLogFile.appendText("ERROR: ${service}(): $message\n");
                }
                LogType.LOG_LEVEL_ABORT -> {
                    theLogFile.appendText("ABORT: ${service}(): $message\n");
                }
                LogType.LOG_LEVEL_STDERR -> {
                    theLogFile.appendText("$message\n");
                }
            }
        }
        catch(e: Exception)
        {
            thisCallFailed = true;
        }
    }
}
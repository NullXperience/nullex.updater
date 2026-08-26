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
    fun safeShellExecution(commandsToExecute: Array<String>): Int
    {
        return try {
            val proc = Runtime.getRuntime().exec(commandsToExecute);
            val stderr = proc.errorStream.bufferedReader().use { it.readText() };
            val stdout = proc.inputStream.bufferedReader().use { it.readText() };
            proc.waitFor();
            consoleLog(LogType.LOG_LEVEL_STDERR, "safeShellExecution", stderr.ifBlank { stdout });
            proc.exitValue();
        }
        catch(e: Exception) {
            consoleLog(LogType.LOG_LEVEL_ERROR, "safeShellExecution", "Failed to run given command");
            consoleLog(LogType.LOG_LEVEL_ERROR, "safeShellExecution", e.toString());
            -1;
        }
    }
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
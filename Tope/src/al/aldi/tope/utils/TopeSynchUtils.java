package al.aldi.tope.utils;

import static al.aldi.tope.utils.TopeCommands.*;

import java.util.HashMap;

import al.aldi.tope.R;

public class TopeSynchUtils {

    HashMap<String, Integer> actionTitlesMap = new HashMap<String, Integer>();

    public TopeSynchUtils() {
        createTitles();
    }

    public int getTitle(String hashFullCommand) {
        int title = 0;
        if (actionTitlesMap.containsKey(hashFullCommand)) {
            Integer tempTitle = actionTitlesMap.get(hashFullCommand);
            if (null != tempTitle) {
                title = tempTitle;
            }
        }
        return title;
    }

    private void createTitles() {

        /* *************************************************************************** */
        /* ***************************** OS ************************************ */
        /* *************************************************************************** */
        actionTitlesMap.put(OS_HIBERNATE, R.string.os_op_hibernate);
        actionTitlesMap.put(OS_LOCK_INPUT, R.string.os_op_lockinput);
        actionTitlesMap.put(OS_LOCK_SCREEN, R.string.os_op_lockscreen);
        actionTitlesMap.put(OS_LOG_OUT, R.string.os_op_logoff);
        actionTitlesMap.put(OS_MONITOR_OFF, R.string.os_op_monitoroff);
        actionTitlesMap.put(OS_MONITOR_ON, R.string.os_op_monitoron);
        actionTitlesMap.put(OS_SHUTDOWN, R.string.os_op_shutdown);
        actionTitlesMap.put(OS_RESTART, R.string.os_op_restart);
        actionTitlesMap.put(OS_SOUND_ON, R.string.os_op_soundon);
        actionTitlesMap.put(OS_SOUND_OFF, R.string.os_op_soundoff);
        actionTitlesMap.put(OS_STAND_BY, R.string.os_op_standby);
        actionTitlesMap.put(OS_TEST, R.string.title_test);
        actionTitlesMap.put(OS_UNLOCK_INPUT, R.string.os_op_unlockinput);

        /* *************************************************************************** */
        /* ***************************** PROG **************************************** */
        /* *************************************************************************** */

        actionTitlesMap.put(PROG_BROWSER_OPEN_URL, R.string.prog_op_openBrowserWithUrl);
        actionTitlesMap.put(PROG_POWERPOINT, R.string.prog_op_controlPowerpoint);
        actionTitlesMap.put(PROG_VLC, R.string.prog_op_controlVLC);

        /* *************************************************************************** */
        /* ***************************** UTILS *************************************** */
        /* *************************************************************************** */
        actionTitlesMap.put(UTIL_SHOW_MSG, R.string.util_op_sendMessage);
        actionTitlesMap.put(UTIL_BEEP, R.string.util_op_beep);
        actionTitlesMap.put(UTIL_READ_OUT_LOUD, R.string.util_op_textToSpeech);
        actionTitlesMap.put(UTIL_READ_CLIPBOARD, R.string.util_op_readClipboard);
    }
}

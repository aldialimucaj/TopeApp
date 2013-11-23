package al.aldi.tope.utils;

import al.aldi.tope.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import static al.aldi.tope.utils.TopeCommands.*;

/**
 * Here you can define a title and the icon an action is going to take.
 * You have to declare the action before in {@link TopeCommands}
 *
 */
public class TopeSynchUtils {

    // matches the action to the titles
    HashMap<String, Integer> actionTitlesMap     = new HashMap<String, Integer>();
    // matches the actions to the icons which are shown in the fragments
    HashMap<String, Integer> commandIconMap      = new HashMap<String, Integer>();
    // if the action should be ignored and not shown in the fragments
    Vector<String>           ignoreActions       = new Vector<String>();
    // if the action needs confirmation before being executed
    Vector<String>           confirmationActions = new Vector<String>();

    public TopeSynchUtils() {
        createTitles();
        createIcons();
        createIgnoreActions();
        createConfirmationActions();
    }

    private void createIgnoreActions() {
        ignoreActions.add(UTIL_PING);
        ignoreActions.add(OS_TEST);
    }

    public boolean isIgnored(String hashFullCommand) {
        for (Iterator<String> iterator = ignoreActions.iterator(); iterator.hasNext();) {
            String type = (String) iterator.next();
            if (type.equals(hashFullCommand)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isConfirmationNeeded(String hashFullCommand) {
        for (Iterator<String> iterator = confirmationActions.iterator(); iterator.hasNext();) {
            String type = (String) iterator.next();
            if (type.equals(hashFullCommand)) {
                return true;
            }
        }
        return false;
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

    public int getIcon(String hashFullCommand) {
        int icon = 0;
        if (commandIconMap.containsKey(hashFullCommand)) {
            Integer tempIcon = commandIconMap.get(hashFullCommand);
            if (null != tempIcon) {
                icon = tempIcon;
            }
        }
        return icon;
    }
    private void createConfirmationActions() {
        confirmationActions.add(OS_LOG_OUT);
        confirmationActions.add(OS_RESTART);
        confirmationActions.add(OS_SHUTDOWN);
        confirmationActions.add(OS_STAND_BY);
        confirmationActions.add(OS_HIBERNATE);
    }

    /**
     * Maps icons for all define actions.
     */
    private void createIcons() {
        /* *************************************************************************** */
        /* *********************************** OS ************************************ */
        /* *************************************************************************** */
        commandIconMap.put(OS_HIBERNATE, R.drawable.system_hibernate);
        commandIconMap.put(OS_LOCK_INPUT, R.drawable.system_input_keyboard);
        commandIconMap.put(OS_LOCK_SCREEN, R.drawable.system_lock_screen);
        commandIconMap.put(OS_LOG_OUT, R.drawable.system_log_out);
        commandIconMap.put(OS_MONITOR_OFF, R.drawable.system_monitor_off);
        commandIconMap.put(OS_MONITOR_ON, R.drawable.system_monitor);
        commandIconMap.put(OS_SHUTDOWN, R.drawable.system_shutdown);
        commandIconMap.put(OS_RESTART, R.drawable.system_restart);
        commandIconMap.put(OS_SOUND_ON, R.drawable.system_sound_on);
        commandIconMap.put(OS_SOUND_OFF, R.drawable.system_sound_off);
        commandIconMap.put(OS_STAND_BY, R.drawable.system_standby);
        commandIconMap.put(OS_TEST, R.drawable.info);
        commandIconMap.put(OS_UNLOCK_INPUT, R.drawable.system_input_keyboard_blocked);
        commandIconMap.put(OS_WAKE_ON_LAN, R.drawable.system_wake_on_lan);

        /* *************************************************************************** */
        /* ***************************** PROG **************************************** */
        /* *************************************************************************** */
        commandIconMap.put(PROG_BROWSER_OPEN_URL, R.drawable.progs_chromium_browser);
        commandIconMap.put(PROG_POWERPOINT, R.drawable.progs_impress);
        commandIconMap.put(PROG_VLC, R.drawable.progs_vlc);

        /* *************************************************************************** */
        /* ***************************** UTILS *************************************** */
        /* *************************************************************************** */
        commandIconMap.put(UTIL_SHOW_MSG, R.drawable.info);
        commandIconMap.put(UTIL_BEEP, R.drawable.utils_bell);
        commandIconMap.put(UTIL_READ_OUT_LOUD, R.drawable.utils_text_to_speech);
        commandIconMap.put(UTIL_READ_CLIPBOARD, R.drawable.utils_copy_clipboard);
        commandIconMap.put(UTIL_WRITE_CLIPBOARD, R.drawable.utils_paste_clipboard);
        commandIconMap.put(UTIL_QUIT_TOPE, R.drawable.utils_quit_tope);
        commandIconMap.put(UTIL_SHORTCUTS, R.drawable.utils_shortcuts);
    }

    /**
     * Map titles to all actions
     */
    private void createTitles() {

        /* *************************************************************************** */
        /* *********************************** OS ************************************ */
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
        actionTitlesMap.put(OS_WAKE_ON_LAN, R.string.os_op_wakeOnLan);

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
        actionTitlesMap.put(UTIL_WRITE_CLIPBOARD, R.string.util_op_writeClipboard);
        actionTitlesMap.put(UTIL_QUIT_TOPE, R.string.util_op_quitTope);
        actionTitlesMap.put(UTIL_SHORTCUTS, R.string.util_op_shortcuts);
    }
}

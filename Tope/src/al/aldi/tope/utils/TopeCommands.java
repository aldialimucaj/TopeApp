package al.aldi.tope.utils;

/**
 * Collection of all Tope calls.
 * 
 * @author Aldi Alimucaj
 * 
 */
public class TopeCommands {

    /* *********************************** OS **************************************** */

    public static final String OS_LOCK_SCREEN        = "/os/lockScreen";
    public static final String OS_LOG_ON             = "/os/logon";
    public static final String OS_STAND_BY           = "/os/standbyPC";
    public static final String OS_HIBERNATE          = "/os/hibernatePC";
    public static final String OS_LOG_OUT            = "/os/logOffPC";
    public static final String OS_SHUTDOWN           = "/os/powerOffPC";
    public static final String OS_RESTART            = "/os/restartPC";
    public static final String OS_LOCK_INPUT         = "/os/lockInput";
    public static final String OS_UNLOCK_INPUT       = "/os/unlockInput";
    public static final String OS_MONITOR_ON         = "/os/turnMonitorOn";
    public static final String OS_MONITOR_OFF        = "/os/turnMonitorOff";
    public static final String OS_SOUND_ON           = "/os/soundOn";
    public static final String OS_SOUND_OFF          = "/os/soundMute";
    public static final String OS_SOUND_EXACT        = "/os/sound_EXACT";
    public static final String OS_TEST               = "/os/test";

    public static final String OS_SYNCH_ACTIONS      = "/os/synchActions";

    /* ************************************* PROG ************************************** */

    public static final String PROG_BROWSER_OPEN_URL = "/prog/openBrowserWithUrl";
    public static final String PROG_POWERPOINT       = "/prog/appControlPowerPoint";
    public static final String PROG_VLC              = "/prog/appControlVLC";

    /* ************************************* PROG ************************************** */

    public static final String UTIL_SHOW_MSG         = "/util/showMsg";
    public static final String UTIL_BEEP             = "/util/beep";
    public static final String UTIL_READ_OUT_LOUD    = "/util/readOutLoud";
    public static final String UTIL_READ_CLIPBOARD   = "/util/readClipBoard";
    public static final String UTIL_PING             = "/util/ping";
    public static final String UTIL_QUIT_TOPE        = "/util/quitTope";
    public static final String UTIL_SHORTCUTS        = "/util/shortcuts";

    /* ************************************* HTTP ************************************** */

    public static String       ARROW_RIGHT           = "#RIGHT";
    public static String       ARROW_LEFT            = "#LEFT";
    public static String       SPACE                 = "#SPACE";
    public static String       BACKSPACE             = "#BACKSPACE";
    public static String       BLACK_OUT             = "#PERIOD";
    public static String       FULL_SCREEN           = "#SHIFTF5";
    
    public static String       CTRL_LEFT             = "#CTRL-LEFT";
    public static String       CTRL_RIGHT            = "#CTRL-RIGHT";
    public static String       CTRL_UP               = "#CTRL-UP";
    public static String       CTRL_DOWN             = "#CTRL-DOWN";
    
    public static String       ALT_F4                = "#ALT-F4";

}

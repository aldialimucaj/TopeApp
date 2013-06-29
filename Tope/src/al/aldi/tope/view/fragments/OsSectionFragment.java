package al.aldi.tope.view.fragments;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.R;
import al.aldi.tope.controller.executables.TestExecutor;
import al.aldi.tope.utils.TopeActionUtils;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class OsSectionFragment extends GeneralSectionFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String               ARG_SECTION_NUMBER       = "section_number";

    public static final String               INTENT_CLICKED_ACTION    = "ACTION";
    public static final String               INTENT_CLICKED_ACTION_ID = "ACTION_ID";

    public static final String               ACTION_PREFIX            = "/os/";


    /* ******************* ITopeActions ******************** */

    public OsSectionFragment() {
        sectionActions = TopeActionUtils.TopeActionUtilsManager.getOsActionUtil();
        actions = sectionActions.getActions();
        super.ACTION_PREFIX = ACTION_PREFIX;

    }
  

    protected void setExecutorsMap() {
        executorMap.put(OS_TEST, new TestExecutor(this));
    }

    protected void setOppositeActionsMap() {
        oppositeActionsMap.put(OS_LOCK_INPUT, OS_UNLOCK_INPUT);
        oppositeActionsMap.put(OS_MONITOR_OFF, OS_MONITOR_ON);
        oppositeActionsMap.put(OS_SOUND_OFF, OS_SOUND_ON);

    }

    protected void fillIconMap() {
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

    }


    @Override
    protected void postRenderingActions() {
        // TODO Auto-generated method stub
        
    }
}

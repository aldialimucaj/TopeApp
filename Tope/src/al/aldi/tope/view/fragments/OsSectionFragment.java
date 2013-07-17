package al.aldi.tope.view.fragments;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.controller.executables.TestExecutor;
import al.aldi.tope.controller.executables.WakeOnLanExecutor;
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
        executorMap.put(OS_WAKE_ON_LAN, new WakeOnLanExecutor(this));
    }

    protected void setOppositeActionsMap() {
        oppositeActionsMap.put(OS_LOCK_INPUT, OS_UNLOCK_INPUT);
        oppositeActionsMap.put(OS_MONITOR_OFF, OS_MONITOR_ON);
        oppositeActionsMap.put(OS_SOUND_OFF, OS_SOUND_ON);

    }

    @Override
    protected void postRenderingActions() {
        // TODO Auto-generated method stub
        
    }
}

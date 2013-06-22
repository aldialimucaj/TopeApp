package al.aldi.tope.view;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.R;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.utils.TopeActionUtils;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class UtilsSectionFragment extends GeneralSectionFragment {

    public static final String ACTION_PREFIX      = "/util/";


    /* ******************* ITopeActions ******************** */

    public UtilsSectionFragment() {
        sectionActions = TopeActionUtils.TopeActionUtilsManager.getUtilsActionUtil();
        actions = sectionActions.getActions();
        super.ACTION_PREFIX = ACTION_PREFIX;
    }

    protected void fillTitlesMap() {
        actionTitlesMap.put(UTIL_SHOW_MSG, getString(R.string.util_op_sendMessage));
        actionTitlesMap.put(UTIL_BEEP, getString(R.string.util_op_beep));
        
    }

    protected void setExecutorsMap() {
        executorMap.put(UTIL_SHOW_MSG, new CallWithArgsExecutor(this));
    }

    protected void setOppositeActionsMap() {

    }

    protected void fillIconMap() {
        commandIconMap.put(UTIL_SHOW_MSG, R.drawable.info);
        commandIconMap.put(UTIL_BEEP, R.drawable.utils_bell);
    }

    @Override
    protected void postRenderingActions() {
        clickBehaviourMap.put(UTIL_SHOW_MSG, ActionClickBehaviour.BEHAVE_BOTH_LONG_CLICK);
        
    }
}

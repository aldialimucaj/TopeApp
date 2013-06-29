package al.aldi.tope.view.fragments;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.controller.executables.ClioboardPayloadExecutor;
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


    protected void setExecutorsMap() {
        executorMap.put(UTIL_SHOW_MSG, new CallWithArgsExecutor(this));
        executorMap.put(UTIL_READ_OUT_LOUD, new CallWithArgsExecutor(this));
        executorMap.put(UTIL_READ_CLIPBOARD, new ClioboardPayloadExecutor(this));
    }

    protected void setOppositeActionsMap() {

    }


    @Override
    protected void postRenderingActions() {
        clickBehaviourMap.put(UTIL_SHOW_MSG, ActionClickBehaviour.BEHAVE_BOTH_LONG_CLICK);
        clickBehaviourMap.put(UTIL_READ_OUT_LOUD, ActionClickBehaviour.BEHAVE_BOTH_LONG_CLICK);
    }
}



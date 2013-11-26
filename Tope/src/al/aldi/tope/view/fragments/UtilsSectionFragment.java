package al.aldi.tope.view.fragments;

import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.controller.executables.ClipboardReadPayloadExecutor;
import al.aldi.tope.controller.executables.ClipboardWritePayloadExecutor;
import al.aldi.tope.controller.executables.ShortcutsExecutor;
import al.aldi.tope.utils.TopeActionUtils;

import static al.aldi.tope.utils.TopeCommands.*;

/**
 * Section Fragment for the Utils Actions. Here you can add new executors the the actions.
 */
public class UtilsSectionFragment extends GeneralSectionFragment {

    public static final String ACTION_PREFIX      = "/util/";


    /* ******************* ITopeActions ******************** */

    public UtilsSectionFragment() {
        sectionActions = TopeActionUtils.TopeActionUtilsManager.getUtilsActionUtil();
        executorMap =  sectionActions.getExecutorMap();
        actions = sectionActions.getActions();
        super.ACTION_PREFIX = ACTION_PREFIX;
        setExecutorsMap();
    }

    /**
     * Define new executors to actions which have specific functions. If the action is
     * making a simple call you dont need to add an extra executor.
     */
    protected void setExecutorsMap() {
        executorMap.put(UTIL_SHOW_MSG, new CallWithArgsExecutor(this));
        executorMap.put(UTIL_READ_OUT_LOUD, new CallWithArgsExecutor(this));
        executorMap.put(UTIL_READ_CLIPBOARD, new ClipboardReadPayloadExecutor(this));
        executorMap.put(UTIL_WRITE_CLIPBOARD, new ClipboardWritePayloadExecutor(this));
        executorMap.put(UTIL_QUIT_TOPE, new CallWithArgsExecutor(this));
        executorMap.put(UTIL_SHORTCUTS, new ShortcutsExecutor(this));
    }

    protected void setOppositeActionsMap() {

    }

    /**
     * This is where you define what happens when an action is clicked. Should it be considered as a specific behavior.
     */
    @Override
    protected void postRenderingActions() {
        clickBehaviourMap.put(UTIL_SHOW_MSG, ActionClickBehaviour.BEHAVE_BOTH_LONG_CLICK);
        clickBehaviourMap.put(UTIL_READ_OUT_LOUD, ActionClickBehaviour.BEHAVE_BOTH_LONG_CLICK);
        clickBehaviourMap.put(UTIL_SHORTCUTS, ActionClickBehaviour.BEHAVE_BOTH_CLICK);
    }
}



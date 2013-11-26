package al.aldi.tope.view.fragments;

import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.controller.executables.PresenationExecutor;
import al.aldi.tope.controller.executables.VlcExecutor;
import al.aldi.tope.utils.TopeActionUtils;

import static al.aldi.tope.utils.TopeCommands.*;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ProgramSectionFragment extends GeneralSectionFragment {

    public static final String ACTION_PREFIX      = "/prog/";

    /* ******************* ITopeActions ******************** */

    public ProgramSectionFragment() {
        sectionActions = TopeActionUtils.TopeActionUtilsManager.getProgActionUtil();
        executorMap =  sectionActions.getExecutorMap();
        actions = sectionActions.getActions();
        super.ACTION_PREFIX = ACTION_PREFIX;
    }

    protected void setExecutorsMap() {
        executorMap.put(PROG_BROWSER_OPEN_URL, new CallWithArgsExecutor(this));
        executorMap.put(PROG_POWERPOINT, new PresenationExecutor(this));
        executorMap.put(PROG_VLC, new VlcExecutor(this));
    }

    protected void setOppositeActionsMap() {

    }

    @Override
    protected void postRenderingActions() {
        clickBehaviourMap.put(PROG_BROWSER_OPEN_URL, ActionClickBehaviour.BEHAVE_BOTH_LONG_CLICK);
        
    }
}
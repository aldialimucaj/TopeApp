package al.aldi.tope.view.fragments;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.R;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.controller.executables.PresenationExecutor;
import al.aldi.tope.controller.executables.VlcExecutor;
import al.aldi.tope.utils.TopeActionUtils;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ProgramSectionFragment extends GeneralSectionFragment {

    public static final String ACTION_PREFIX      = "/prog/";

    /* ******************* ITopeActions ******************** */

    public ProgramSectionFragment() {
        sectionActions = TopeActionUtils.TopeActionUtilsManager.getProgActionUtil();
        actions = sectionActions.getActions();
        super.ACTION_PREFIX = ACTION_PREFIX;
    }

    protected void fillTitlesMap() {
        actionTitlesMap.put(PROG_BROWSER_OPEN_URL, getString(R.string.prog_op_openBrowserWithUrl));
        actionTitlesMap.put(PROG_POWERPOINT, getString(R.string.prog_op_controlPowerpoint));
        actionTitlesMap.put(PROG_VLC, getString(R.string.prog_op_controlVLC));
    }

    protected void setExecutorsMap() {
        executorMap.put(PROG_BROWSER_OPEN_URL, new CallWithArgsExecutor(this));
        executorMap.put(PROG_POWERPOINT, new PresenationExecutor(this));
        executorMap.put(PROG_VLC, new VlcExecutor(this));
    }

    protected void setOppositeActionsMap() {

    }

    protected void fillIconMap() {
        commandIconMap.put(PROG_BROWSER_OPEN_URL, R.drawable.progs_chromium_browser);
        commandIconMap.put(PROG_POWERPOINT, R.drawable.progs_impress);
        commandIconMap.put(PROG_VLC, R.drawable.progs_vlc);
    }

    @Override
    protected void postRenderingActions() {
        clickBehaviourMap.put(PROG_BROWSER_OPEN_URL, ActionClickBehaviour.BEHAVE_BOTH_LONG_CLICK);
        
    }
}
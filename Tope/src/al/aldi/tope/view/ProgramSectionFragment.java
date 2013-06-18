package al.aldi.tope.view;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.R;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.controller.executables.PresenationExecutor;
import al.aldi.tope.utils.TopeActionUtils;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ProgramSectionFragment extends GeneralSectionFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

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

    }

    protected void setExecutorsMap() {
        executorMap.put(PROG_BROWSER_OPEN_URL, new CallWithArgsExecutor(this));
        executorMap.put(PROG_POWERPOINT, new PresenationExecutor(this));
    }

    protected void setOppositeActionsMap() {

    }

    protected void fillIconMap() {
        commandIconMap.put(PROG_BROWSER_OPEN_URL, R.drawable.progs_chromium_browser);
        commandIconMap.put(PROG_POWERPOINT, R.drawable.progs_impress);

    }
}
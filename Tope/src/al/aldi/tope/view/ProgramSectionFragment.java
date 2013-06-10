package al.aldi.tope.view;

import static al.aldi.tope.utils.TopeCommands.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.controller.executables.DefaultExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.utils.TopeActionUtils;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.adapter.IconItemAdapter;
import al.aldi.tope.view.listeners.ActionClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ProgramSectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String               ARG_SECTION_NUMBER = "section_number";

    public static final String               ACTION_PREFIX      = "/prog/";

    GridView                                 gridView           = null;

    IconItemAdapter<ITopeAction>             adapter            = null;
    TopeActionUtils                          progActions          = null;
    Vector<ITopeAction>                      actions            = null;
    private HashMap<String, Integer>         commandIconMap     = new HashMap<String, Integer>();
    private HashMap<String, String>          oppositeActionsMap = new HashMap<String, String>();
    private HashMap<String, ITopeExecutable> executorMap        = new HashMap<String, ITopeExecutable>();
    private HashMap<String, String>          actionTitlesMap    = new HashMap<String, String>();

    /* ******************* ITopeActions ******************** */
    ITopeAction                              testAction         = null;

    public ProgramSectionFragment() {
        progActions = TopeActionUtils.TopeActionUtilsManager.getProgActionUtil();
        actions = progActions.getActions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("ProgSectionFragment.onCreateView()");

        /* preparing actions */
        fillIconMap();
        fillTitlesMap();
        setExecutorsMap();
        setOppositeActionsMap();

        /* finished preparing actions */

        final View rootView = inflater.inflate(R.layout.gridview_fragment_prog, container, false);

        gridView = (GridView) rootView.findViewById(R.id.fragmentGridView);
        registerForContextMenu(gridView);

        /* init the commands to show in the screen */
        initCommandsAutomatically();

        /* creating the grid adapter */
        adapter = new IconItemAdapter<ITopeAction>(getActivity(), actions);
        adapter.setFragment(this);

        gridView.setAdapter(adapter);

        /* This is the main listener for the actions */
        gridView.setOnItemClickListener(new ActionClickListener(actions, getActivity()));


        return rootView;
    }


    private void initCommandsAutomatically() {
        actions.clear(); /* clearing the cached activities before recreating them */

        ActionDataSource dataSource = new ActionDataSource(getActivity());
        List<TopeAction> dbActions = dataSource.getAll();

        /* filter the actions in order to get just those with the Fragment prefix */
        dbActions = TopeUtils.filterActions(dbActions, ACTION_PREFIX);

        for (Iterator<TopeAction> iterator = dbActions.iterator(); iterator.hasNext();) {
            TopeAction topeAction = (TopeAction) iterator.next();
            String command = topeAction.getCommandFullPath();
            topeAction.setItemId(commandIconMap.get(command));

            /* setting the executor */
            if (executorMap.containsKey(topeAction.getCommandFullPath())) {
                ITopeExecutable exe = executorMap.get(topeAction.getCommandFullPath());
                exe.setAction(topeAction);
                topeAction.setExecutable(exe);
            } else {
                topeAction.setExecutable(new DefaultExecutor(topeAction, this));
            }

            /* setting the title */
            if (actionTitlesMap.containsKey(topeAction.getCommandFullPath())) {
                topeAction.setTitle(actionTitlesMap.get(topeAction.getCommandFullPath()));
            }

            /* setting opposite actions */
            setOpposite(dbActions, topeAction);

            /* adding the action if it is not an opposite action. */
            if (!isOppositeAction(topeAction)) {
                actions.add(topeAction);
            }

        }
    }

    private boolean isOppositeAction(TopeAction action) {
        return oppositeActionsMap.containsValue(action.getCommandFullPath());
    }

    private void setOpposite(List<TopeAction> dbActions, TopeAction action) {
        if (!action.hasOppositeAction() && oppositeActionsMap.containsKey(action.getCommandFullPath())) {

            for (Iterator<TopeAction> iterator = dbActions.iterator(); iterator.hasNext();) {
                TopeAction oppositeAction = (TopeAction) iterator.next();
                if (oppositeAction.getCommandFullPath().equals(oppositeActionsMap.get(action.getCommandFullPath()))) {
                    action.setOppositeAction(oppositeAction);
                    oppositeAction.setOppositeAction(action);
                }
            }

        }
    }

    private void fillTitlesMap() {
        actionTitlesMap.put(PROG_BROWSER_OPEN_URL, getString(R.string.prog_op_openBrowserWithUrl));


    }

    private void setExecutorsMap() {
        executorMap.put(PROG_BROWSER_OPEN_URL, new CallWithArgsExecutor(this));
    }

    private void setOppositeActionsMap() {

    }

    private void fillIconMap() {
        commandIconMap.put(PROG_BROWSER_OPEN_URL, R.drawable.prog_chromium_browser);


    }
}
package al.aldi.tope.view;

import static al.aldi.tope.utils.TopeCommands.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.executables.DefaultExecutor;
import al.aldi.tope.controller.executables.TestExecutor;
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
public class OsSectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String               ARG_SECTION_NUMBER       = "section_number";

    public static final String               INTENT_CLICKED_ACTION    = "ACTION";
    public static final String               INTENT_CLICKED_ACTION_ID = "ACTION_ID";

    public static final String               OS_ACTION_PREFIX            = "/os/";

    GridView                                 gridView                 = null;

    IconItemAdapter<ITopeAction>             adapter                  = null;
    TopeActionUtils                          osActions                = null;
    Vector<ITopeAction>                      actions                  = null;
    HashMap<TopeAction, Integer>             dbActionsMap             = null;
    private HashMap<String, Integer>         commandIconMap           = new HashMap<String, Integer>();
    private HashMap<String, String>          oppositeActionsMap       = new HashMap<String, String>();
    private HashMap<String, ITopeExecutable> executorMap              = new HashMap<String, ITopeExecutable>();
    private HashMap<String, String>          actionTitlesMap          = new HashMap<String, String>();

    /* ******************* ITopeActions ******************** */
    ITopeAction                              testAction               = null;

    public OsSectionFragment() {
        osActions = TopeActionUtils.TopeActionUtilsManager.getOsActionUtil();
        actions = osActions.getActions();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("OsSectionFragment.onCreateView()");
        fillIconMap();
        fillTitlesMap();
        setExecutorsMap();
        setOppositeActionsMap();
        final View rootView = inflater.inflate(R.layout.gridview_fragment_os, container, false);

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
    @Override
    public void onStart() {
        /* init the commands to show in the screen */
        initCommandsAutomatically();
        /* creating the grid adapter */
        adapter = new IconItemAdapter<ITopeAction>(getActivity(), actions);
        adapter.setFragment(this);

        gridView.setAdapter(adapter);

        /* This is the main listener for the actions */
        gridView.setOnItemClickListener(new ActionClickListener(actions, getActivity()));

        super.onStart();
        System.out.println("OsSectionFragment.onStart()");

    }


    private void initCommandsAutomatically() {
        actions.clear(); /* clearing the cached activities before recreating them */

        ActionDataSource actionDataSource = new ActionDataSource(getActivity());
        actionDataSource.open();
        HashMap<TopeAction, Integer> dbActionsMap = actionDataSource.getAllOccurencies();
        List<TopeAction> dbActions = new Vector<TopeAction>(dbActionsMap.keySet());

        /* filter the actions in order to get just those with the Fragment prefix */
        dbActions = TopeUtils.filterActions(dbActions, OS_ACTION_PREFIX);

        for (Iterator<TopeAction> iterator = dbActions.iterator(); iterator.hasNext();) {
            TopeAction topeAction = (TopeAction) iterator.next();
            int actionOccurency = dbActionsMap.get(topeAction);
            if (actionOccurency <= 0) {
                continue;// the actions is not found at all in the client set
            }
            if (actionOccurency < dbActions.size()) {
                // the actions is not found IN all clients
            }
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

        actionDataSource.close();
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
        actionTitlesMap.put(OS_HIBERNATE, getString(R.string.os_op_hibernate));
        actionTitlesMap.put(OS_LOCK_INPUT, getString(R.string.os_op_lockinput));
        actionTitlesMap.put(OS_LOCK_SCREEN, getString(R.string.os_op_lockscreen));
        actionTitlesMap.put(OS_LOG_OUT, getString(R.string.os_op_logoff));
        actionTitlesMap.put(OS_MONITOR_OFF, getString(R.string.os_op_monitoroff));
        actionTitlesMap.put(OS_MONITOR_ON, getString(R.string.os_op_monitoron));
        actionTitlesMap.put(OS_SHUTDOWN, getString(R.string.os_op_shutdown));
        actionTitlesMap.put(OS_RESTART, getString(R.string.os_op_restart));
        actionTitlesMap.put(OS_SOUND_ON, getString(R.string.os_op_soundon));
        actionTitlesMap.put(OS_SOUND_OFF, getString(R.string.os_op_soundoff));
        actionTitlesMap.put(OS_STAND_BY, getString(R.string.os_op_standby));
        actionTitlesMap.put(OS_TEST, getString(R.string.title_test));
        actionTitlesMap.put(OS_UNLOCK_INPUT, getString(R.string.os_op_unlockinput));

    }

    private void setExecutorsMap() {
        executorMap.put(OS_TEST, new TestExecutor(this));
    }

    private void setOppositeActionsMap() {
        oppositeActionsMap.put(OS_LOCK_INPUT, OS_UNLOCK_INPUT);
        oppositeActionsMap.put(OS_MONITOR_OFF, OS_MONITOR_ON);
        oppositeActionsMap.put(OS_SOUND_OFF, OS_SOUND_ON);

    }

    private void fillIconMap() {
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
}

package al.aldi.tope.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeExecutable;
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
public abstract class GeneralSectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String       ARG_SECTION_NUMBER       = "section_number";

    public static final String       INTENT_CLICKED_ACTION    = "ACTION";
    public static final String       INTENT_CLICKED_ACTION_ID = "ACTION_ID";

    String       ACTION_PREFIX            = "";

    GridView                         gridView                 = null;
    int                              fragmentId               = R.layout.gridview_fragment;
    int                              fragmentGridId           = R.id.fragmentGridView;

    IconItemAdapter<ITopeAction>     adapter                  = null;
    TopeActionUtils                  sectionActions           = null;
    Vector<ITopeAction>              actions                  = null;
    HashMap<TopeAction, Integer>     dbActionsMap             = null;
    HashMap<String, Integer>         commandIconMap           = new HashMap<String, Integer>();
    HashMap<String, String>          oppositeActionsMap       = new HashMap<String, String>();
    HashMap<String, ITopeExecutable> executorMap              = new HashMap<String, ITopeExecutable>();
    HashMap<String, String>          actionTitlesMap          = new HashMap<String, String>();

    /* ******************* ITopeActions ******************** */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("OsSectionFragment.onCreateView()");
        fillIconMap();
        fillTitlesMap();
        setExecutorsMap();
        setOppositeActionsMap();
        final View rootView = inflater.inflate(fragmentId, container, false);

        gridView = (GridView) rootView.findViewById(fragmentGridId);
        registerForContextMenu(gridView);

        /* init the commands to show in the screen */
        initCommandsAutomatically();

        /* creating the grid adapter */
        adapter = new IconItemAdapter<ITopeAction>(getActivity(), actions, dbActionsMap);
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
        adapter = new IconItemAdapter<ITopeAction>(getActivity(), actions, dbActionsMap);
        adapter.setFragment(this);

        gridView.setAdapter(adapter);

        /* This is the main listener for the actions */
        gridView.setOnItemClickListener(new ActionClickListener(actions, getActivity()));

        super.onStart();
        System.out.println("OsSectionFragment.onStart()");

    }

    protected void initCommandsAutomatically() {
        actions.clear(); /* clearing the cached activities before recreating them */

        ActionDataSource actionDataSource = new ActionDataSource(getActivity());
        actionDataSource.open();
        dbActionsMap = actionDataSource.getAllOccurencies();
        List<TopeAction> dbActions = new Vector<TopeAction>(dbActionsMap.keySet());

        /* filter the actions in order to get just those with the Fragment prefix */
        dbActions = TopeUtils.filterActions(dbActions, ACTION_PREFIX);

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

    protected boolean isOppositeAction(TopeAction action) {
        return oppositeActionsMap.containsValue(action.getCommandFullPath());
    }

    protected void setOpposite(List<TopeAction> dbActions, TopeAction action) {
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

    protected abstract void fillTitlesMap();

    protected abstract void setExecutorsMap();

    protected abstract void setOppositeActionsMap();

    protected abstract void fillIconMap();
}
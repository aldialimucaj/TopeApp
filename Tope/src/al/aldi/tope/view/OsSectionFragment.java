package al.aldi.tope.view;

import static al.aldi.tope.utils.TopeCommands.*;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.utils.TopeActionUtils;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.adapter.IconItemAdapter;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
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
    public static final String		ARG_SECTION_NUMBER			= "section_number";

    public static final String		INTENT_CLICKED_ACTION		= "ACTION";
    public static final String		INTENT_CLICKED_ACTION_ID	= "ACTION_ID";

    GridView						gridView					= null;

    IconItemAdapter<ITopeAction>	adapter						= null;
    TopeActionUtils					osActions					= null;
    Vector<ITopeAction>				actions						= null;

    /* ******************* ITopeActions ******************** */
    ITopeAction						testAction					= null;

    public OsSectionFragment() {
        osActions = TopeActionUtils.TopeActionUtilsManager.getOsActionUtil();
        actions = osActions.getActions();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("OsSectionFragment.onCreateView()");
        final View rootView = inflater.inflate(R.layout.gridview_fragment_os, container, false);

        gridView = (GridView) rootView.findViewById(R.id.fragmentGridView);
        registerForContextMenu(gridView);

        /* init the commands to show in the screen */
        initCommands();

        /* creating the grid adapter */
        adapter = new IconItemAdapter<ITopeAction>(getActivity(), actions);
        adapter.setFragment(this);

        gridView.setAdapter(adapter);

        /* This is the main listener for the actions */
        gridView.setOnItemClickListener(new ActionClickListener(actions, getActivity()));

        /* TODO: REMOVE */
        // initLongClickListeners();

        return rootView;
    }

    private void initCommands() {
        ClientDataSource source = new ClientDataSource(getActivity().getApplicationContext());
        TopeUtils topeUtils = new TopeUtils(source);

        actions.clear(); /* clearing the cached activities before recreating them */

        /* *************************************************************************** */

        actions.add(topeUtils.addAction(OS_POWER_OFF, R.drawable.system_shutdown, getString(R.string.os_op_shutdown)));

        /* *************************************************************************** */

        actions.add(topeUtils.addAction(OS_RESTART, R.drawable.system_restart, getString(R.string.os_op_restart)));

        /* *************************************************************************** */

        actions.add(topeUtils.addAction(OS_HIBERNATE, R.drawable.system_hibernate, getString(R.string.os_op_hibernate)));

        /* *************************************************************************** */

        ITopeAction standByAction = topeUtils.addAction(OS_STAND_BY, R.drawable.system_standby, getString(R.string.os_op_standby));
        actions.add(standByAction);
        osActions.setViewActions(standByAction, new StandardActionDialog1(getActivity(), standByAction, this));

        /* *************************************************************************** */

        actions.add(topeUtils.addAction(OS_LOCK_SCREEN, R.drawable.system_lock_screen, getString(R.string.os_op_lockscreen)));

        /* *************************************************************************** */

        ITopeAction monitorOn = topeUtils.addAction(OS_MONITOR_ON, R.drawable.system_monitor, getString(R.string.os_op_monitoron));
        ITopeAction monitorOff = topeUtils.addAction(OS_MONITOR_OFF, R.drawable.system_monitor_off, getString(R.string.os_op_monitoroff));
        monitorOn.setOppositeAction(monitorOff);
        monitorOff.setOppositeAction(monitorOn);
        actions.add(monitorOff);

        /* *************************************************************************** */

        ITopeAction action1 = topeUtils.addAction(OS_LOCK_INPUT, R.drawable.input_keyboard, getString(R.string.os_op_lockinput));
        ITopeAction action2 = topeUtils.addAction(OS_UNLOCK_INPUT, R.drawable.input_keyboard_blocked, getString(R.string.os_op_unlockinput));
        action1.setOppositeAction(action2);
        action2.setOppositeAction(action1);
        actions.add(action1);

        /* *************************************************************************** */

        ITopeAction soundOff = topeUtils.addAction(OS_SOUND_OFF, R.drawable.system_sound_off, getString(R.string.os_op_soundoff));
        ITopeAction soundOn = topeUtils.addAction(OS_SOUND_ON, R.drawable.system_sound_on, getString(R.string.os_op_soundon));
        soundOff.setOppositeAction(soundOn);
        soundOn.setOppositeAction(soundOff);
        actions.add(soundOff);

        /* *************************************************************************** */

        testAction = topeUtils.addAction(OS_TEST, R.drawable.info, getString(R.string.title_test));
        actions.add(testAction);
        // osActions.setViewActions(testAction, new TestActionDialog(getActivity(), testAction));
        osActions.setViewActions(testAction, new StandardActionDialog1(getActivity(), testAction, this));

    }

}

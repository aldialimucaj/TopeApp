package al.aldi.tope.view;

import static al.aldi.tope.utils.TopeCommands.*;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.executables.DefaultExecutor;
import al.aldi.tope.controller.executables.TestExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.TestResponse;
import al.aldi.tope.utils.TopeActionUtils;
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
    public static final String              ARG_SECTION_NUMBER       = "section_number";

    public static final String              INTENT_CLICKED_ACTION    = "ACTION";
    public static final String              INTENT_CLICKED_ACTION_ID = "ACTION_ID";

    GridView                                gridView                 = null;

    IconItemAdapter<ITopeAction>            adapter                  = null;
    TopeActionUtils                         osActions                = null;
    Vector<ITopeAction>                     actions                  = null;

    /* ******************* ITopeActions ******************** */
    ITopeAction<TopeResponse<TestResponse>> testAction               = null;

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
        actions.clear(); /* clearing the cached activities before recreating them */

        /* *************************************************************************** */

        ITopeAction<TopeResponse<TestResponse>> powerOffAction = new TopeAction<TopeResponse<TestResponse>>(OS_POWER_OFF, R.drawable.system_shutdown, getString(R.string.os_op_shutdown));
        actions.add(powerOffAction);

        /* *************************************************************************** */

        ITopeAction<TopeResponse<TestResponse>> restartAction = new TopeAction<TopeResponse<TestResponse>>(OS_RESTART, R.drawable.system_restart, getString(R.string.os_op_restart));
        actions.add(restartAction);

        /* *************************************************************************** */

        ITopeAction<TopeResponse<TestResponse>> hibernateAction = new TopeAction<TopeResponse<TestResponse>>(OS_HIBERNATE, R.drawable.system_hibernate, getString(R.string.os_op_hibernate));
        actions.add(hibernateAction);

        /* *************************************************************************** */

        ITopeAction<TopeResponse<TestResponse>> standByAction = new TopeAction<TopeResponse<TestResponse>>(OS_STAND_BY, R.drawable.system_standby, getString(R.string.os_op_standby));
        standByAction.setActionId(2);
        standByAction.setExecutable(new TestExecutor(standByAction, this));
        actions.add(standByAction);

        /* *************************************************************************** */

        // UP TO DATE
        ITopeAction<TopeResponse<Object>> lockScreen = new TopeAction<TopeResponse<Object>>(OS_LOCK_SCREEN, R.drawable.system_lock_screen, getString(R.string.os_op_lockscreen));
        lockScreen.setActionId(6);
        lockScreen.setExecutable(new DefaultExecutor<TopeResponse<Object>>(lockScreen, this));
        actions.add(lockScreen);

        /* *************************************************************************** */

        ITopeAction<TopeResponse<TestResponse>> monitorOn = new TopeAction<TopeResponse<TestResponse>>(OS_MONITOR_ON, R.drawable.system_monitor, getString(R.string.os_op_monitoron));
        ITopeAction<TopeResponse<TestResponse>> monitorOff = new TopeAction<TopeResponse<TestResponse>>(OS_MONITOR_OFF, R.drawable.system_monitor_off, getString(R.string.os_op_monitoroff));
        monitorOn.setOppositeAction(monitorOff);
        monitorOff.setOppositeAction(monitorOn);
        actions.add(monitorOff);

        /* *************************************************************************** */

        ITopeAction<TopeResponse<TestResponse>> lockInput = new TopeAction<TopeResponse<TestResponse>>(OS_LOCK_INPUT, R.drawable.input_keyboard, getString(R.string.os_op_lockinput));
        ITopeAction<TopeResponse<TestResponse>> unlockInput = new TopeAction<TopeResponse<TestResponse>>(OS_UNLOCK_INPUT, R.drawable.input_keyboard_blocked, getString(R.string.os_op_unlockinput));
        lockInput.setOppositeAction(unlockInput);
        unlockInput.setOppositeAction(lockInput);
        actions.add(lockInput);

        /* *************************************************************************** */

        ITopeAction<TopeResponse<TestResponse>> soundOff = new TopeAction<TopeResponse<TestResponse>>(OS_SOUND_OFF, R.drawable.system_sound_off, getString(R.string.os_op_soundoff));
        ITopeAction<TopeResponse<TestResponse>> soundOn = new TopeAction<TopeResponse<TestResponse>>(OS_SOUND_ON, R.drawable.system_sound_on, getString(R.string.os_op_soundon));
        soundOff.setOppositeAction(soundOn);
        soundOn.setOppositeAction(soundOff);
        soundOff.setActionId(11);
        soundOn.setActionId(12);
        actions.add(soundOff);

        /* *************************************************************************** */

        testAction = new TopeAction<TopeResponse<TestResponse>>(OS_TEST, R.drawable.info, getString(R.string.title_test));
        testAction.setExecutable(new TestExecutor(testAction, this));
        actions.add(testAction);

        // osActions.setViewActions(testAction, new TestActionDialog(getActivity(), testAction));

    }

}

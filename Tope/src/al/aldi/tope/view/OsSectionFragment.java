package al.aldi.tope.view;

import static al.aldi.tope.TopeCommands.*;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.TopeUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.model.db.ClientDataSource;
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
    public static final String		ARG_SECTION_NUMBER			= "section_number";

    public static final String		INTENT_CLICKED_ACTION		= "ACTION";
    public static final String		INTENT_CLICKED_ACTION_ID	= "ACTION_ID";

    GridView						gridView					= null;
    IconItemAdapter<ITopeAction>	adapter						= null;
    Vector<ITopeAction>				items						= new Vector<ITopeAction>();

    /* ******************* ITopeActions ******************** */
    ITopeAction						testAction					= null;

    public OsSectionFragment() {

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
        adapter = new IconItemAdapter<ITopeAction>(getActivity(), items);

        gridView.setAdapter(adapter);

        /* This is the main listener for the actions */
        gridView.setOnItemClickListener(new ActionClickListener(items, getActivity()));


        /*TODO: REMOVE */
        //initLongClickListeners();

        return rootView;
    }


    private void initCommands() {
        ClientDataSource source = new ClientDataSource(getActivity().getApplicationContext());
        TopeUtils topeUtils = new TopeUtils(source);

        items.clear(); /* clearing the cached activities before recreating them */

        items.add(topeUtils.addAction(OS_POWER_OFF, R.drawable.system_shutdown, getString(R.string.os_op_shutdown)));

        items.add(topeUtils.addAction(OS_RESTART, R.drawable.system_restart, getString(R.string.os_op_restart)));

        items.add(topeUtils.addAction(OS_HIBERNATE, R.drawable.system_hibernate, getString(R.string.os_op_hibernate)));

        items.add(topeUtils.addAction(OS_STAND_BY, R.drawable.system_standby, getString(R.string.os_op_standby)));

        items.add(topeUtils.addAction(OS_LOCK_SCREEN, R.drawable.system_lock_screen, getString(R.string.os_op_lockscreen)));

        ITopeAction monitorOn = topeUtils.addAction(OS_MONITOR_ON, R.drawable.system_monitor, getString(R.string.os_op_monitoron));
        ITopeAction monitorOff = topeUtils.addAction(OS_MONITOR_OFF, R.drawable.system_monitor_off, getString(R.string.os_op_monitoroff));
        monitorOn.setOppositeAction(monitorOff);
        monitorOff.setOppositeAction(monitorOn);
        items.add(monitorOff);

        ITopeAction action1 = topeUtils.addAction(OS_LOCK_INPUT, R.drawable.input_keyboard, getString(R.string.os_op_lockinput));
        ITopeAction action2 = topeUtils.addAction(OS_UNLOCK_INPUT, R.drawable.input_keyboard_blocked, getString(R.string.os_op_unlockinput));
        action1.setOppositeAction(action2);
        action2.setOppositeAction(action1);
        items.add(action1);

        ITopeAction soundOff = topeUtils.addAction(OS_SOUND_OFF, R.drawable.system_sound_off, getString(R.string.os_op_soundoff));
        ITopeAction soundOn = topeUtils.addAction(OS_SOUND_ON, R.drawable.system_sound_on, getString(R.string.os_op_soundon));
        soundOff.setOppositeAction(soundOn);
        soundOn.setOppositeAction(soundOff);
        items.add(soundOff);

        testAction = topeUtils.addAction(OS_TEST, R.drawable.info, getString(R.string.title_test));

        items.add(testAction);

    }

}

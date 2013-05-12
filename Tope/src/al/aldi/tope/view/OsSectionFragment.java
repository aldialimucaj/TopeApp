package al.aldi.tope.view;

import static al.aldi.tope.TopeCommands.OS_HIBERNATE;
import static al.aldi.tope.TopeCommands.OS_LOCK_INPUT;
import static al.aldi.tope.TopeCommands.OS_LOCK_SCREEN;
import static al.aldi.tope.TopeCommands.OS_MONITOR_OFF;
import static al.aldi.tope.TopeCommands.OS_MONITOR_ON;
import static al.aldi.tope.TopeCommands.OS_POWER_OFF;
import static al.aldi.tope.TopeCommands.OS_RESTART;
import static al.aldi.tope.TopeCommands.OS_STAND_BY;
import static al.aldi.tope.TopeCommands.OS_TEST;
import static al.aldi.tope.TopeCommands.OS_UNLOCK_INPUT;

import java.util.Vector;

import al.aldi.android.test.TestLib;
import al.aldi.tope.R;
import al.aldi.tope.TopeCommands;
import al.aldi.tope.TopeUtils;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.view.adapter.IconItemAdapter;
import al.aldi.tope.view.listeners.ActionClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class OsSectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String	ARG_SECTION_NUMBER			= "section_number";

    public static final String	INTENT_CLICKED_ACTION		= "ACTION";
    public static final String	INTENT_CLICKED_ACTION_ID	= "ACTION_ID";

    GridView					gridView;
    Vector<ITopeAction>			items						= new Vector<ITopeAction>();

    public OsSectionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("OsSectionFragment.onCreateView()");
        final View rootView = inflater.inflate(R.layout.gridview_fragment_os, container, false);

        gridView = (GridView) rootView.findViewById(R.id.fragmentGridView);
        registerForContextMenu(gridView);

        initCommands(); /* init the commands to show in the screen */

        IconItemAdapter<ITopeAction> adapter = new IconItemAdapter<ITopeAction>(getActivity(), items);

        gridView.setAdapter(adapter);

        /**
         * This is the main listener for the actions
         */
        gridView.setOnItemClickListener(new ActionClickListener(items, getActivity()));

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Add parameters");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Add parameters") {
            ITopeAction action = TopeUtils.getAction(items, info.id);
            Intent i = new Intent(this.getActivity(), ParametersActivity.class);

            i.putExtra(INTENT_CLICKED_ACTION, action);

            startActivity(i);
            // Toast.makeText(this.getActivity(), "Add parameters called from "+ TopeUtils.getAction(items, info.id).getTitle(),
            // Toast.LENGTH_SHORT).show();
        }
        return true;
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

        ITopeAction soundOff = topeUtils.addAction(TopeCommands.OS_SOUND_OFF, R.drawable.system_sound_off, getString(R.string.os_op_soundoff));
        ITopeAction soundOn =  topeUtils.addAction(TopeCommands.OS_SOUND_ON, R.drawable.system_sound_on, getString(R.string.os_op_soundon));
        soundOff.setOppositeAction(soundOn);
        soundOn.setOppositeAction(soundOff);
        items.add(soundOff);

        items.add(topeUtils.addAction(OS_TEST, R.drawable.info, getString(R.string.title_test)));

    }

}

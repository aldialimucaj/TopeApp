package al.aldi.tope.view;

import static al.aldi.tope.TopeCommands.*;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.TopeUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.view.adapter.IconItemAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    public static final String	ARG_SECTION_NUMBER	= "section_number";

    GridView					gridView;
    Vector<ITopeAction>			items				= new Vector<ITopeAction>();

    public OsSectionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("OsSectionFragment.onCreateView()");
        final View rootView = inflater.inflate(R.layout.gridview_fragment_os, container, false);
        // final Resources res = getResources();

        gridView = (GridView) rootView.findViewById(R.id.fragmentGridView);

        initCommands(); /* init the commands to show in the screen */

        IconItemAdapter<ITopeAction> adapter = new IconItemAdapter<ITopeAction>(getActivity(), items);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ((ITopeAction) items.elementAt(position)).execute();
                ITopeAction action = ((ITopeAction) items.elementAt(position));
                if (action.hasOppositeAction()) {

                    ImageView imageView = (ImageView) v.findViewById(R.id.gridActionImage);
                    imageView.setImageResource(action.getOppositeAction().getItemId());

                    TextView tv1 = (TextView) v.findViewById(R.id.gridActionText);
                    tv1.setText(action.getOppositeAction().getTitle());

                    action = action.getOppositeAction();
                    items.set(position, action);
                }
            }
        });
        return rootView;
    }

    private void initCommands() {

        items.clear(); /* clearing the cached activities before recreating them */

        items.add(TopeUtils.addAction(OS_POWER_OFF, R.drawable.system_shutdown, getString(R.string.os_op_shutdown)));

        items.add(TopeUtils.addAction(OS_RESTART, R.drawable.system_restart, getString(R.string.os_op_restart)));

        items.add(TopeUtils.addAction(OS_HIBERNATE, R.drawable.system_hibernate, getString(R.string.os_op_hibernate)));

        items.add(TopeUtils.addAction(OS_STAND_BY, R.drawable.system_standby, getString(R.string.os_op_standby)));

        items.add(TopeUtils.addAction(OS_LOCK_SCREEN, R.drawable.system_lock_screen, getString(R.string.os_op_lockscreen)));

        ITopeAction action1 = TopeUtils.addAction(OS_LOCK_INPUT, R.drawable.input_keyboard, getString(R.string.os_op_lockinput));
        ITopeAction action2 = TopeUtils.addAction(OS_UNLOCK_INPUT, R.drawable.input_keyboard_blocked, getString(R.string.os_op_unlockinput));
        action1.setOppositeAction(action2);
        action2.setOppositeAction(action1);
        items.add(action1);

    }

}
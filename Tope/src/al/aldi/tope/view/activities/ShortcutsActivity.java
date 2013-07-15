package al.aldi.tope.view.activities;

import static al.aldi.tope.utils.TopeCommands.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.utils.TopeUtils;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShortcutsActivity extends ListActivity {

    private static String   SHORTCUTS      = "shortcuts";

    Fragment                fragment       = null;
    Vibrator                vibrator       = null;
    HashMap<String, String> shortuctsTitle = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcuts);
        // Show the Up button in the action bar.
        setupActionBar();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setupKeyMap();

        ArrayList<String> values = new ArrayList<String>();
        values.addAll(shortuctsTitle.keySet());
        Collections.sort(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_simple_no_desc, values);
        setListAdapter(adapter);
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                ITopeAction topeAction = new TopeAction(SHORTCUTS, 0, getString(R.string.util_op_shortcuts));
                topeAction.setCommandFullPath(UTIL_SHORTCUTS);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, shortuctsTitle.get(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, ShortcutsActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);
            }

        });
    }

    private void setupKeyMap() {
        shortuctsTitle.put("Alt-F4", ALT_F4);
        shortuctsTitle.put("Escape", ESCAPE);
        shortuctsTitle.put("Enter", ENTER);
        shortuctsTitle.put("Alt-Tab", ALT_TAB);
        shortuctsTitle.put("Space", SPACE);
        shortuctsTitle.put("Page Up", PAGE_UP);
        shortuctsTitle.put("Page Down", PAGE_DOWN);
        shortuctsTitle.put("Ctrl-W", CTRL_W);
        shortuctsTitle.put("Ctrl-TAB", CTRL_TAB);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shortcuts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

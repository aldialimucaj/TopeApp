package al.aldi.tope.view.dialog.fragment;

import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.model.ITopeAction;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * A template for creating new action dialogs
 */
public class TestActionDialog extends ListView {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    public TestActionDialog(Context context) {
        super(context);
    }

    public TestActionDialog(final Context context, final ITopeAction action) {
        super(context);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        setAdapter(arrayAdapter);
        arrayAdapter.add("Send Test Message");
        arrayAdapter.add("Do nothing");

        setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (position == 0) {
                    ActionCareTaker act = new ActionCareTaker(action, (Activity) context);
                    act.execute();
                }
            }

        });

    }

}

package al.aldi.tope.view;

import java.util.HashMap;

import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.adapter.ParametersActivityAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ParametersActivity extends Activity {

    TopePayload				payload			= new TopePayload();
    HashMap<String, String>	items			= payload.getParameters();
    ITopeAction				action			= null;

    LinearLayout			linearLayout	= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_parameters);
        Intent intent = getIntent();

        action = (ITopeAction) intent.getParcelableExtra(OsSectionFragment.INTENT_CLICKED_ACTION);

        ClientDataSource source = new ClientDataSource(this.getApplicationContext());
        TopeUtils topeUtils = new TopeUtils(source);
        action = topeUtils.addAction(action.getMethod(), action.getItemId(), action.getTitle());

        linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_parameters, null);
        // linearLayout = (LinearLayout) findViewById(R.layout.activity_parameters);

        ParametersActivityAdapter adapter = new ParametersActivityAdapter(payload.getPayloads(), this);

        int i = 0;
        for (@SuppressWarnings("unused") String key : payload.getPayloads()) {
            // LinearLayout innerLinearLayout = (LinearLayout) inflater.inflate(R.layout.activity_parameters_items, null);
            View v = adapter.getView(i, null, linearLayout);
            linearLayout.addView(v);
            i++;
        }

        View controllButtons = (LinearLayout) inflater.inflate(R.layout.activity_parameters_control_buttons, null);
        linearLayout.addView(controllButtons);

        setContentView(linearLayout);

        // Show the Up button in the action bar.
        setupActionBar();
        initListeners();
    }

    private void initListeners() {

        Button executeButton = (Button) findViewById(R.id.buttonExecute);
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = linearLayout.getChildCount();

                for (int i = 0; i < childCount; i++) {
                    View innerView = linearLayout.getChildAt(i);
                    EditText etx = (EditText) innerView.findViewById(R.id.actionValue);
                    TextView tx = (TextView) innerView.findViewById(R.id.actionName);
                    if (null != etx && null != tx) {
                        if (!etx.getText().toString().equals("")) {
                            String action = tx.getText().toString();
                            String value = etx.getText().toString();
                            try {
                                payload.addPayload(action, value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                action.setPayload(payload);
                ActionCareTaker act = new ActionCareTaker(action, ParametersActivity.this);
                act.execute();
            }

        });

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ParametersActivity.this);
            }

        });

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
        getMenuInflater().inflate(R.menu.parameters, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

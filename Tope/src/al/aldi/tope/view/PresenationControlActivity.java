package al.aldi.tope.view;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopePayload;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class PresenationControlActivity extends Activity {

    private static String NEXT       = "SPACE";
    Fragment              fragment   = null;
    

    public PresenationControlActivity() {
        super();
    }

    public PresenationControlActivity(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenation_control);
        // Show the Up button in the action bar.
        setupActionBar();
        initListeners();
    }

    private void initListeners() {
        ImageButton next = (ImageButton) findViewById(R.id.imageButtonNext);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction("appControllPowerPoint", 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_POWERPOINT);
                topeAction.setActionId(0);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);


                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, NEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                }  

                ActionCareTaker act = new ActionCareTaker(topeAction, PresenationControlActivity.this);
                act.execute();

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
        getMenuInflater().inflate(R.menu.presenation_control, menu);
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

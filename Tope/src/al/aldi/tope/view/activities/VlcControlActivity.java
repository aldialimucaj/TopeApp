package al.aldi.tope.view.activities;

import static al.aldi.tope.utils.TopeCommands.*;
import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.utils.TopeUtils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class VlcControlActivity extends Activity {

    private static final String APP_CONTROLL = "appControlVLC";

    Fragment                 fragment                 = null;
    Vibrator                 vibrator                 = null;

    public VlcControlActivity() {
        super();
    }

    public VlcControlActivity(Fragment fragment) {
        this();
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlc_control);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Show the Up button in the action bar.
        setupActionBar();
        initListeners();
    }

    private void initListeners() {
        Button next = (Button) findViewById(R.id.vlc_buttonFullScreen);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction(APP_CONTROLL, 0, getString(R.string.prog_op_controlVLC));
                topeAction.setCommandFullPath(PROG_VLC);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, "#f");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, VlcControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });

        ImageButton buttonBackwards = (ImageButton) findViewById(R.id.vlc_imageButtonBackwards);
        buttonBackwards.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction(APP_CONTROLL, 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_VLC);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, CTRL_LEFT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, VlcControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });

        ImageButton buttonForwards = (ImageButton) findViewById(R.id.vlc_imageButtonForwards);
        buttonForwards.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction(APP_CONTROLL, 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_VLC);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, CTRL_RIGHT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, VlcControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });

        ImageButton fullScreeButton = (ImageButton) findViewById(R.id.vlc_buttonPlayPause);
        fullScreeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction(APP_CONTROLL, 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_VLC);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, SPACE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, VlcControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });
        
        ImageButton soundLowButton = (ImageButton) findViewById(R.id.vlc_buttonSoundLow);
        soundLowButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction(APP_CONTROLL, 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_VLC);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, CTRL_DOWN);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, VlcControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });
        
        ImageButton soundHighButton = (ImageButton) findViewById(R.id.vlc_buttonSoundHigh);
        soundHighButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction(APP_CONTROLL, 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_VLC);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, CTRL_UP);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, VlcControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });
        
        Button buttonShowTime = (Button) findViewById(R.id.vlc_buttonShowTime);
        buttonShowTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction(APP_CONTROLL, 0, getString(R.string.prog_op_controlVLC));
                topeAction.setCommandFullPath(PROG_VLC);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, "#t");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, VlcControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

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
        //getMenuInflater().inflate(R.menu.presenation_control, menu);
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

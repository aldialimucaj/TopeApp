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
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PresenationControlActivity extends Activity implements OnClickListener {
    // private static final String TAG = "al.aldi.tope.view.PresenationControlActivity";

    private static final int SWIPE_MIN_DISTANCE       = 80;
    private static final int SWIPE_MAX_OFF_PATH       = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public static final int  SWIPE_VIBRATION_TIME     = 10;

    Fragment                 fragment                 = null;

    private GestureDetector  gestureDetector;
    View.OnTouchListener     gestureListener;
    Vibrator                 vibrator                 = null;

    public PresenationControlActivity() {
        super();
    }

    public PresenationControlActivity(Fragment fragment) {
        this();
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenation_control);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                vibrator.vibrate(PresenationControlActivity.SWIPE_VIBRATION_TIME);
                return gestureDetector.onTouchEvent(event);
            }
        };

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
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, ARROW_RIGHT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, PresenationControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });

        ImageButton prev = (ImageButton) findViewById(R.id.imageButtonPrevious);
        prev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction("appControllPowerPoint", 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_POWERPOINT);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, ARROW_LEFT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, PresenationControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });

        Button blackOut = (Button) findViewById(R.id.presentation_buttonBlackout);
        blackOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction("appControllPowerPoint", 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_POWERPOINT);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, BLACK_OUT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, PresenationControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });

        Button fullScreeButton = (Button) findViewById(R.id.presentation_buttonFullScreen);
        fullScreeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = new TopeAction("appControllPowerPoint", 0, getString(R.string.prog_op_controlPowerpoint));
                topeAction.setCommandFullPath(PROG_POWERPOINT);
                topeAction.setActionId(0);
                topeAction.setOutputIgnored(true);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                topeAction.setExecutable(executor);

                try {
                    topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, FULL_SCREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(topeAction, PresenationControlActivity.this);
                act.execute();
                vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.presentation_imageViewLeftRight);

        // Do this for each view added to the grid
        imageView.setOnClickListener((OnClickListener) PresenationControlActivity.this);
        imageView.setOnTouchListener(gestureListener);

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

    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    ITopeAction topeAction = new TopeAction("appControllPowerPoint", 0, getString(R.string.prog_op_controlPowerpoint));
                    topeAction.setCommandFullPath(PROG_POWERPOINT);
                    topeAction.setActionId(0);
                    topeAction.setOutputIgnored(true);
                    CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                    topeAction.setExecutable(executor);

                    try {
                        topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, BACKSPACE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ActionCareTaker act = new ActionCareTaker(topeAction, PresenationControlActivity.this);
                    act.execute();
                    vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    ITopeAction topeAction = new TopeAction("appControllPowerPoint", 0, getString(R.string.prog_op_controlPowerpoint));
                    topeAction.setCommandFullPath(PROG_POWERPOINT);
                    topeAction.setActionId(0);
                    topeAction.setOutputIgnored(true);
                    CallWithArgsExecutor executor = new CallWithArgsExecutor(topeAction, getApplicationContext());
                    topeAction.setExecutable(executor);

                    try {
                        topeAction.getPayload().addPayload(TopePayload.PARAM_ARG_0, SPACE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ActionCareTaker act = new ActionCareTaker(topeAction, PresenationControlActivity.this);
                    act.execute();
                    vibrator.vibrate(TopeUtils.TOPE_ACTION_CLICK_VIBRATION_SHORT);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

    }

    @Override
    public void onClick(View v) {
    }

}

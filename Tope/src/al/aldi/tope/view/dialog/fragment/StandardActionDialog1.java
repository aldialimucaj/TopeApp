package al.aldi.tope.view.dialog.fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

import al.aldi.tope.R;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.model.JsonTopeResponse;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

/**
 * Standard Action Dialog which offers an execution date and a timer for the action.
 *
 * @author Aldi Alimucaj
 *
 */
public class StandardActionDialog1 extends LinearLayout implements ITopeActionDialog {
    protected static final String TAG                 = "StandardActionDialog1";
    LinearLayout.LayoutParams     layoutParams        = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
    Button                        buttonExecutionDate = null;
    Button                        buttonTimer         = null;

    public StandardActionDialog1(Context context) {
        super(context);
    }

    public StandardActionDialog1(final ITopeAction action, final Fragment fragment) {
        super(fragment.getActivity());
        layoutParams.setMargins(10, 0, 10, 0);

        LayoutInflater li = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) li.inflate(R.layout.dialog_standard1, this, false);

        buttonExecutionDate = (Button) ll.findViewById(R.id.dialog_name_setExecutionDate);

        buttonTimer = (Button) ll.findViewById(R.id.dialog_name_setTimer);

        buttonTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment df = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        final LinearLayout ll1 = new LinearLayout(getContext());
                        ll1.setOrientation(LinearLayout.HORIZONTAL);
                        ll1.setGravity(Gravity.CENTER);

                        final NumberPicker npMinutes = new NumberPicker(getContext());
                        npMinutes.setPadding(10, 0, 0, 0);
                        npMinutes.setMinValue(0);
                        npMinutes.setMaxValue(59);
                        final NumberPicker npHours = new NumberPicker(getContext());
                        // npHours.setPadding(10, 0, 0, 0);
                        npHours.setMinValue(0);
                        npHours.setMaxValue(23);

                        ll1.addView(npHours, layoutParams);
                        ll1.addView(npMinutes, layoutParams);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder

                        .setMessage("Set Timer (HH:MM)")// TODO: you might wanna replace this a custom title

                                .setView(ll1)

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        int timeInMin = npMinutes.getValue();
                                        int timeInHrs = npHours.getValue();
                                        buttonTimer.setText(timeInHrs + " Hours " + timeInMin + " Min");
                                        int timerSum = (timeInHrs * 60 * 60) + (timeInMin * 60);
                                        ITopePayload payload = action.getPayload();
                                        try {
                                            payload.addPayload(TopePayload.PARAM_TIME_TO_WAIT, String.valueOf(timerSum * 1000));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        return builder.create();
                    }
                };

                df.show(fragment.getChildFragmentManager(), "TAG");

            }
        });

        buttonExecutionDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment df = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        final LinearLayout ll2 = new LinearLayout(getContext());
                        ll2.setOrientation(LinearLayout.VERTICAL);
                        final TimePicker tp = new TimePicker(getContext());
                        tp.setIs24HourView(true);
                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        tp.setCurrentHour(hour);
                        tp.setCurrentMinute(minute);

                        final DatePicker dp = new DatePicker(getContext());
                        dp.setCalendarViewShown(false);
                        ll2.addView(tp);
                        ll2.addView(dp);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder

                        .setMessage("Set Time and Date")// TODO: you might wanna replace this a custom title

                                .setView(ll2)

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        tp.clearFocus();
                                        GregorianCalendar gc = new GregorianCalendar(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute());
                                        buttonExecutionDate.setText(tp.getCurrentHour() + ":" + tp.getCurrentMinute() + " on " + dp.getYear() + "/" + dp.getMonth() + "/" + dp.getDayOfMonth());
                                        ITopePayload payload = action.getPayload();
                                        try {
                                            Log.i(TAG, "Executing at: " + JsonTopeResponse.formatDate(gc.getTime()) + " " + gc.getTimeInMillis());
                                            payload.addPayload(TopePayload.PARAM_TIME_TO_EXEC, JsonTopeResponse.formatDate(gc.getTime()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        return builder.create();
                    }
                };

                df.show(fragment.getChildFragmentManager(), "TAG");

            }
        });

        addView(ll);

    }

    @Override
    public void cleanUp() {
        if (null != buttonExecutionDate) {
            buttonExecutionDate.setText(R.string.dialog_text_setExecDate);
        }

        if (null != buttonTimer) {
            buttonTimer.setText(R.string.dialog_text_setTimer);
        }

    }

    @Override
    public void setUp() {
        // TODO Auto-generated method stub

    }
}

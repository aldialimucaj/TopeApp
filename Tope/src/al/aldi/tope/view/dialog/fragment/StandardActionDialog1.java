package al.aldi.tope.view.dialog.fragment;

import java.util.Date;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopePayload;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class StandardActionDialog1 extends LinearLayout {
    protected static final String	TAG					= "StandardActionDialog1";
    LinearLayout.LayoutParams		layoutParams		= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    Button							buttonExecutionDate	= null;
    Button							buttonTimer			= null;

    public StandardActionDialog1(Context context) {
        super(context);
    }

    public StandardActionDialog1(final Context context, final ITopeAction action, final Fragment fragment) {
        super(context);

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) li.inflate(R.layout.dialog_standard1, this, false);

        buttonExecutionDate = (Button) ll.findViewById(R.id.dialog_name_setExecutionDate);
        buttonTimer = (Button) ll.findViewById(R.id.dialog_name_setTimer);
        // TimePicker tp = (TimePicker) ll.findViewById(R.id.dialog_timePicker1);
        // tp.setIs24HourView(true);

        buttonTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment df = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        final EditText tv = new EditText(getContext());

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder

                        .setMessage("Set Timer")// TODO: you might wanna replace this a custom title

                                .setView(tv)

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        int timeInSec = Integer.valueOf(tv.getText().toString());
                                        buttonTimer.setText(timeInSec + " Secs");
                                        ITopePayload payload = action.getPayload();
                                        try {
                                            payload.addPayload(TopePayload.PARAM_TIME_TO_WAIT, String.valueOf(timeInSec*1000));
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
                        final TimePicker tp = new TimePicker(getContext());

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder

                        .setMessage("Set Time and Date")// TODO: you might wanna replace this a custom title

                                .setView(tp)

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        tp.clearFocus();
                                        buttonExecutionDate.setText(tp.getCurrentHour() + ":" + tp.getCurrentMinute());
                                        Date date = new Date(System.currentTimeMillis());
                                        date.setHours(Integer.valueOf(tp.getCurrentHour()));
                                        date.setMinutes(Integer.valueOf(tp.getCurrentMinute()));
                                        ITopePayload payload = action.getPayload();
                                        try {
                                            payload.addPayload(TopePayload.PARAM_TIME_TO_EXEC, date.toString());
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
}

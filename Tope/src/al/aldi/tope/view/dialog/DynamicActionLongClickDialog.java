package al.aldi.tope.view.dialog;

import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.utils.TopeActionUtils;
import al.aldi.tope.view.dialog.fragment.ITopeActionDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The hub resolving the connection between the action and the dynamic dialogs they have registered.
 *
 * @author Aldi Alimucaj
 *
 */
public class DynamicActionLongClickDialog extends DialogFragment {

    public static final String	DIALOG_CANCEL		= "Cancel";
    public static final String	DIALOG_EXECUTE		= "Execute";
    public static final String	DYNAMIC_DIALOG		= "DynamicDialog";
    public static final String	KEY_DYNAMIC_VIEW	= "dynamicView";

    LinearLayout				innerView			= null;
    View						dynamicView			= null;
    LinearLayout.LayoutParams	layoutParams		= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    /* ACTION */
    ITopeAction					action				= null;
    TopeActionUtils				actionUtil			= null;

    public DynamicActionLongClickDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /* just in case there is no dynamic view */
        createDummyView();

        if (null != getArguments() && getArguments().containsKey(KEY_DYNAMIC_VIEW)) {
            Object object = getArguments().get(KEY_DYNAMIC_VIEW);

            if (null != object && object instanceof ITopeAction) {
                action = (ITopeAction) object;
                dynamicView = action.getContextView();

                /* if the view is already registered then we need to unregister it first before reusing it */
                if (null != dynamicView && null != dynamicView.getParent()) {
                    ((ViewGroup) dynamicView.getParent()).removeAllViews();
                }
            }
        }

        /* CREATING THE BUILDER */

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder

        .setMessage(DYNAMIC_DIALOG)// TODO: you might wanna replace this a custom title

                .setView(dynamicView == null ? innerView : dynamicView)

                .setPositiveButton(DIALOG_EXECUTE, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /* ACTION EXECUTION */
                        ActionCareTaker act = new ActionCareTaker(action, getActivity());
                        act.execute();
                    }
                }).setNegativeButton(DIALOG_CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        action.getPayload().clear();
                    }
                });

        cleanUp(dynamicView);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * reset the view if necessary
     *
     * @param v
     */
    private void cleanUp(View v) {
        if (null != v && v instanceof ITopeActionDialog) {
            ((ITopeActionDialog) v).cleanUp();
        }
    }

    private void createDummyView() {
        innerView = new LinearLayout(getActivity());
        innerView.setGravity(Gravity.CENTER);
        innerView.setLayoutParams(layoutParams);
        TextView tv = new TextView(getActivity());
        tv.setText("No Special Powers");
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 10, 10, 10);
        innerView.addView(tv);
    }
}

package al.aldi.tope.utils;

import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.R;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.model.db.ClientDataSource;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.util.Log.e;

/**
 * Utility class for working with tope objects.
 *
 * @author Aldi Alimucaj
 */
public class TopeUtils {
    public static final String LOG_TAG = "Tope";

    public static final String TOPE_DEFAULT_PORT                 = "8503";
    public static final int    TOAST_MAX_LENGHT                  = 50;
    public static final int    TOPE_ACTION_CLICK_VIBRATION_SHORT = 80;
    public static final int    TOPE_ACTION_CLICK_VIBRATION_LONG  = 200;

    private static ClientDataSource clientDataSource = null;
    private static ActionDataSource actionDataSource = null;

    ClientDataSource source = null;

    public TopeUtils(ClientDataSource source) {
        super();
        this.source = source;
    }

    public static ClientDataSource getClientDataSource(Context context) {
        if (null == context && null == clientDataSource) {
            e(LOG_TAG, "TopeUtils.getClientDataSource(): You need a valid context to create a database context");
        } else {
            clientDataSource = new ClientDataSource(context);
        }
        return clientDataSource;
    }

    public static ActionDataSource getActionDataSource(Context context) {
        if (null == context && null == actionDataSource) {
            e(LOG_TAG, "TopeUtils.getActionDataSource(): You need a valid context to create a database context");
        } else {
            actionDataSource = new ActionDataSource(context);
        }
        return actionDataSource;
    }

    /**
     * Get the action out of the list by looking for the item id which it is bound with.
     *
     * @param actions
     * @param id      the Andorid item, like for example the icon.
     * @return the action if found or null
     */
    public static ITopeAction getAction(List<ITopeAction> actions, long id) {
        ITopeAction action = null;
        for (Iterator<ITopeAction> iterator = actions.iterator(); iterator.hasNext(); ) {
            ITopeAction iTopeAction = (ITopeAction) iterator.next();
            if (iTopeAction.getItemId() == id) {
                action = iTopeAction;
            }
        }
        return action;
    }

    /**
     * Get the action out of the list by looking for the item id which it is bound with.
     *
     * @param actions
     * @param v       the Andorid item, like for example the icon.
     * @return the action if found or null
     */
    public static ITopeAction getAction(List<ITopeAction> actions, View v) {
        ITopeAction action = null;
        if ((v instanceof ViewGroup)) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View view = (View) vg.getChildAt(i);
                if ((view instanceof ImageView)) {
                    ImageView imageView = (ImageView) view;
                    Object tagObj = imageView.getTag();
                    int tag = (Integer) tagObj;
                    action = getAction(actions, tag);
                    if (null != action) {
                        return action;
                    }
                }

            }
        }
        return action;
    }

    /**
     * Util method to get back the view.
     *
     * @param map
     * @param action
     * @return
     */
    public static View getViewFromActionMap(HashMap<ITopeAction, View> map, ITopeAction action) {
        if (map.containsKey(action)) {
            return map.get(action);

        }
        e(LOG_TAG, "No action found in ViewList");
        return null;
    }

    /**
     * Print message with trailing TOAST_MAX_LENGHTH check.
     *
     * @param context
     * @param msg
     */
    public static void printMsg(Context context, final String msg) {
        String printMsg = msg;
        printMsg = AldiStringUtils.trailWithThreeDots(msg, TOAST_MAX_LENGHT);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Print success message
     *
     * @param action
     * @param topeResponse
     * @param context
     */
    @SuppressWarnings("rawtypes")
    public static void printSuccessMsg(ITopeAction action, TopeResponse topeResponse, Context context) {
        if (null != topeResponse && !topeResponse.isIgnore()) {
            if (topeResponse.isSuccessful()) {
                printMsg(context, context.getString(R.string.toast_successful_br) + " " + action.getTitle());
            } else {
                String errMsg = "";
                if (null != topeResponse && null != topeResponse.getMessage() && !topeResponse.getMessage().equals("null")) {
                    errMsg = ".\n" + topeResponse.getMessage();
                }
                if (topeResponse.getStatus() == 404) {
                    errMsg += context.getString(R.string.action_not_found);
                }
                printMsg(context, context.getString(R.string.toast_failed_br) + " " + action.getTitle() + " " + errMsg);
            }
        }
    }

    /**
     * Print bulk message success status. It also checks if there is only one call and in that case
     * it calls the single message print out format.
     *
     * @param topeResponses
     * @param action
     * @param context
     */
    @SuppressWarnings("rawtypes")
    public static void printBulkSuccessMsg(List<TopeResponse> topeResponses, ITopeAction action, Context context) {
        /* actions can define if output should be ignored. in this case no print out */
        if (action.isOutputIgnored()) {
            return;
        }

        int size = topeResponses.size();

        /* if there is only one response then use the single msg format */
        if (1 == size) {
            printSuccessMsg(action, topeResponses.get(0), context);
            return;
        }

        int successfullyRun = 0;
        for (Iterator<TopeResponse> iterator = topeResponses.iterator(); iterator.hasNext(); ) {
            TopeResponse topeResponse = (TopeResponse) iterator.next();
            if (topeResponse.isSuccessful()) {
                successfullyRun++;
            }
        }
        Toast.makeText(context, "Executed [" + size + "], Successful {" + successfullyRun + "}, Action (" + action.getTitle() + ")", Toast.LENGTH_LONG).show();
    }
}

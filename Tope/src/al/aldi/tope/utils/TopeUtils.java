package al.aldi.tope.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.view.adapter.ITopeLongClickAdapter;
import al.aldi.tope.view.listeners.ActionLongClickListener;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Utility class for workign with tope objects.
 * 
 * @author Aldi Alimucaj
 * 
 */
public class TopeUtils {
    public static final String LOG_TAG                           = "al.aldi.tope.TopeUtils";

    public static final String TOPE_DEFAULT_PORT                 = "8080";

    public static final int    TOPE_ACTION_CLICK_VIBRATION_SHORT = 80;
    public static final int    TOPE_ACTION_CLICK_VIBRATION_LONG  = 200;

    ClientDataSource           source                            = null;

    public TopeUtils(ClientDataSource source) {
        super();
        this.source = source;
    }

    /**
     * Get the action out of the list by looking for the item id which it is bound with.
     * 
     * @param actions
     * @param id
     *            the Andorid item, like for example the icon.
     * @return the action if found or null
     */
    public static ITopeAction getAction(List<ITopeAction> actions, long id) {
        ITopeAction action = null;
        for (Iterator<ITopeAction> iterator = actions.iterator(); iterator.hasNext();) {
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
     * @param view
     *            the Andorid item, like for example the icon.
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
        Log.e(LOG_TAG, "No action found in ViewList");
        return null;
    }

    public static void addLongClickListener(ITopeLongClickAdapter longClickAdapter, ITopeAction action, Activity activity) {
        HashMap<ITopeAction, View> map = longClickAdapter.getTopeActionViewMap();
        View v = getViewFromActionMap(map, action);
        ActionLongClickListener alcl = new ActionLongClickListener(activity);
        v.setOnLongClickListener(alcl);
    }

    public static void addLongClickListener(View v, ITopeAction action, Activity activity) {
        ActionLongClickListener alcl = new ActionLongClickListener(activity);
        v.setOnLongClickListener(alcl);
    }

    /**
     * Print message.
     * 
     * @param action
     * @param successful
     */
    public static void printMsg(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Print success message
     * 
     * @param action
     * @param topeResponse
     * @param activity
     */
    @SuppressWarnings("rawtypes")
    public static void printSuccessMsg(ITopeAction action, TopeResponse topeResponse, Activity activity) {
        if (null != topeResponse && !topeResponse.isIgnore()) {
            if (topeResponse.isSuccessful()) {
                Toast.makeText(activity, "[Successful] " + action.getTitle(), Toast.LENGTH_LONG).show();
            } else {
                String errMsg = "";
                if (null != topeResponse && null != topeResponse.getMessage() && !topeResponse.getMessage().equals("null")) {
                    errMsg = ".\n" + topeResponse.getMessage();
                }
                Toast.makeText(activity, "[Failed] " + action.getTitle() + errMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Print success message.
     * 
     * @param action
     * @param successful
     * @param activity
     */
    public static void printSuccessMsg(ITopeAction action, boolean successful, Activity activity) {
        if (successful) {
            Toast.makeText(activity, "Successful: " + action.getTitle(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Failed: " + action.getTitle(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Print bulk message success status. It also checks if there is only one call and in that case
     * it calls the single message print out format.
     * 
     * @param topeResponses
     * @param action
     * @param activity
     */
    @SuppressWarnings("rawtypes")
    public static void printBulkSuccessMsg(List<TopeResponse> topeResponses, ITopeAction action, Activity activity) {
        /* actions can define if output should be ignored. in this case no print out */
        if (action.isOutputIgnored()) {
            return;
        }

        int size = topeResponses.size();

        /* if there is only one response then use the single msg format */
        if (1 == size) {
            printSuccessMsg(action, topeResponses.get(0), activity);
            return;
        }

        int successfullyRun = 0;
        for (Iterator<TopeResponse> iterator = topeResponses.iterator(); iterator.hasNext();) {
            TopeResponse topeResponse = (TopeResponse) iterator.next();
            if (topeResponse.isSuccessful()) {
                successfullyRun++;
            }
        }
        Toast.makeText(activity, "Run [" + size + "], Successful {" + successfullyRun + "}, Action (" + action.getTitle() + ")", Toast.LENGTH_LONG).show();
    }

    public static List<TopeAction> filterActions(List<TopeAction> actions, String prefix) {
        List<TopeAction> filteredActions = new Vector<TopeAction>();
        for (Iterator<TopeAction> iterator = actions.iterator(); iterator.hasNext();) {
            TopeAction topeAction = (TopeAction) iterator.next();
            if (topeAction.getCommandFullPath().startsWith(prefix)) {
                filteredActions.add(topeAction);
            }
        }
        return filteredActions;
    }

    public static Vector<ITopeAction> convertActions(List<TopeAction> actions) {
        Vector<ITopeAction> convertedActions = new Vector<ITopeAction>();
        for (Iterator<TopeAction> iterator = actions.iterator(); iterator.hasNext();) {
            ITopeAction topeAction = (ITopeAction) iterator.next();
            convertedActions.add(topeAction);
        }
        return convertedActions;
    }

}

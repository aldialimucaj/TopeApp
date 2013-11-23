package al.aldi.tope.utils;

import al.aldi.tope.model.ITopeAction;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Vector;

/**
 * Utility class for the actions. A hub for common action structure and outsourcing some common functions.
 */
public class TopeActionUtils {
    private static String LOG_TAG = "TopeActionUtils";

    private Vector<ITopeAction>        actions    = new Vector<ITopeAction>();
    private HashMap<ITopeAction, View> actionView = new HashMap<ITopeAction, View>();

    private TopeActionUtils() {
        // no public constructor. Singleton
    }

    public void setViewActions(ITopeAction action, View v) {
        if (actions.contains(action)) {
            actionView.put(action, v);
        } else {
            Log.w(LOG_TAG, "Adding view before adding action into the list.");
        }
    }

    public View getViewFromActions(ITopeAction action) {
        View v = null;
        if (actions.contains(action) && actionView.containsKey(action)) {
            v = actionView.get(action);
        }
        return v;
    }

    public void removeAction(ITopeAction action) {
        if (actions.contains(action)) {
            actions.remove(action);
        }

        if (actionView.containsKey(action)) {
            actionView.remove(action);
        }
    }

    public void clearActions() {
        actions.clear();
        actionView.clear();
    }

    public Vector<ITopeAction> getActions() {
        return actions;
    }

    public void setActions(Vector<ITopeAction> mActions) {
        this.actions = mActions;
    }

    public HashMap<ITopeAction, View> getActionView() {
        return actionView;
    }

    public void setActionView(HashMap<ITopeAction, View> mActionView) {
        this.actionView = mActionView;
    }

    /**
     * The manager contains a singleton set of action types. Stored here for
     * frequent usage in memory.
     */
    public static class TopeActionUtilsManager {
        private static TopeActionUtils smOsActions    = null;
        private static TopeActionUtils smProgActions  = null;
        private static TopeActionUtils smUtilsActions = null;

        public static TopeActionUtils getOsActionUtil() {
            if (null == smOsActions) {
                smOsActions = new TopeActionUtils();
            }
            return smOsActions;
        }

        public static TopeActionUtils getProgActionUtil() {
            if (null == smProgActions) {
                smProgActions = new TopeActionUtils();
            }
            return smProgActions;
        }

        public static TopeActionUtils getUtilsActionUtil() {
            if (null == smUtilsActions) {
                smUtilsActions = new TopeActionUtils();
            }
            return smUtilsActions;
        }

    }

}

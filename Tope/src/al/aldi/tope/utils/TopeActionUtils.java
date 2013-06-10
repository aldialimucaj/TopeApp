package al.aldi.tope.utils;

import java.util.HashMap;
import java.util.Vector;

import al.aldi.tope.model.ITopeAction;
import android.util.Log;
import android.view.View;

public class TopeActionUtils {
    private static String                LOG_TAG        = "TopeActionUtils";

    private Vector<ITopeAction>            actions        = new Vector<ITopeAction>();
    private HashMap<ITopeAction, View>    actionView    = new HashMap<ITopeAction, View>();

    private TopeActionUtils() {
        // no public constructor. Singleton
    }

    public void setViewActions(ITopeAction action, View v) {
        if (actions.contains(action)) {
            actionView.put(action, v);
        }else{
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

    public static class TopeActionUtilsManager {
        private static TopeActionUtils    smOsActions    = null;
        private static TopeActionUtils    smProgActions    = null;

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

    }

}

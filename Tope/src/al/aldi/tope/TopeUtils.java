package al.aldi.tope;

import java.util.Iterator;
import java.util.List;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ClientDataSource;
import android.app.Activity;
import android.widget.Toast;

public class TopeUtils {
    public static final String	TOPE_DEFAULT_PORT	= "8080";

    ClientDataSource			source				= null;

    public TopeUtils(ClientDataSource source) {
        super();
        this.source = source;
    }

    /**
     * Prepares the action to execute. It creates an executable for the action and sets it as the default one.
     * Depending on the presence of payload, it sends a GET or POST request.
     *
     * @param actionStr
     * @param itemId
     * @param title
     * @return
     */
    public ITopeAction addAction(final String actionStr, int itemId, String title) {
        final ITopeAction action = new TopeAction(itemId, title, actionStr);
        action.setExecutable(new ITopeExecutable() {
            @Override
            public boolean run() {
                boolean cleanRun = true; /*
                                         * if there are more then one client to execute then the command was successful only if all were
                                         * successful
                                         */
                source.open();
                List<TopeClient> clients = source.getAllActive(); /* reads all acitve clients from the database */
                for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext();) {
                    TopeClient topeClient = (TopeClient) iterator.next();
                    TopeResponse topeResponse = null;
                    if (action.hasPayload()) { /* Payload is set at runtime after the creation of this anonymous method */
                        topeResponse = HttpUtils.sendPostRequestWithParams(topeClient.getSslURL(actionStr), action.getPayload().getParameters());
                    } else {
                        topeResponse = HttpUtils.sendGetRequest(topeClient.getSslURL(actionStr));
                    }

                    if (null == topeResponse || !topeResponse.isSuccessful()) {
                        cleanRun = false;
                    }
                }
                return cleanRun;
            }
        });

        return action;
    }

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

    public static void printSuccessMsg(ITopeAction action, TopeResponse topeResponse, Activity activity) {
        if (topeResponse.isSuccessful()) {
            Toast.makeText(activity, "Successful: " + action.getTitle(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Failed: " + action.getTitle() + "\n" + topeResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void printSuccessMsg(ITopeAction action, boolean successful, Activity activity) {
        if (successful) {
            Toast.makeText(activity, "Successful: " + action.getTitle(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Failed: " + action.getTitle(), Toast.LENGTH_LONG).show();
        }
    }
}

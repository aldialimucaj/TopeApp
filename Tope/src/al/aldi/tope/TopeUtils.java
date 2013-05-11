package al.aldi.tope;

import java.util.Iterator;
import java.util.List;

import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.utils.TopeHttpUtil;
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
        final ITopeAction action = new TopeAction(itemId, title, actionStr, new TopePayload());

        action.setExecutable(new ITopeExecutable() {
            @Override
            public TopeResponse run(TopeClient topeClient) {
                TopeResponse topeResponse = null;
                try {
                    action.getPayload().addPayload(TopePayload.PARAM_USER, topeClient.getUser());
                    action.getPayload().addPayload(TopePayload.PARAM_PASSWORD, topeClient.getPass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                topeResponse = TopeHttpUtil.sendPostRequestWithParams(topeClient.getSslURL(actionStr), action.getPayload().getParameters());

                return topeResponse;
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
        if (null != topeResponse && topeResponse.isSuccessful()) {
            Toast.makeText(activity, "[Successful] " + action.getTitle(), Toast.LENGTH_LONG).show();
        } else {
            String errMsg = "";
            if(null != topeResponse && null != topeResponse.getMessage())
            {
                errMsg =  ".\n" + topeResponse.getMessage();
            }
            Toast.makeText(activity, "[Failed] " + action.getTitle() + errMsg, Toast.LENGTH_LONG).show();
        }
    }

    public static void printSuccessMsg(ITopeAction action, boolean successful, Activity activity) {
        if (successful) {
            Toast.makeText(activity, "Successful: " + action.getTitle(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Failed: " + action.getTitle(), Toast.LENGTH_LONG).show();
        }
    }

    public static void printBulkSuccessMsg(List<TopeResponse> topeResponses, ITopeAction action, Activity activity) {
        int size = topeResponses.size();

        /* if there is only one response then use the single msg format */
        if(1 == size){
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
        Toast.makeText(activity, "Run [" + size + "], Successful {" + successfullyRun + "}, Action (" + action.getTitle()+")", Toast.LENGTH_LONG).show();
    }

}

package al.aldi.tope;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import al.aldi.andorid.JSONUtils;
import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.view.OsSectionFragment;
import android.app.Activity;
import android.widget.Toast;

public class TopeUtils {
    public static final String	TOPE_DEFAULT_PORT		= "8080";
    public static final String	STR_TRUE				= "true";
    public static final String	STR_FALSE				= "false";
    public static final String	JSON_RES_SUCCESS		= "success";
    public static final String	JSON_RES_STATUS_CODE	= "statusCode";

    ClientDataSource			source					= null;

    public TopeUtils(ClientDataSource source) {
        super();
        this.source = source;
    }

    public ITopeAction addAction(final String actionStr, int itemId, String title) {
        final ITopeAction action = new TopeAction(itemId, title, actionStr);
        action.setExecutable(new ITopeExecutable() {
            @Override
            public boolean run() {
                boolean cleanRun = true;
                source.open();
                List<TopeClient> clients = source.getAllActive();
                for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext();) {
                    TopeClient topeClient = (TopeClient) iterator.next();
                    JSONObject jo = null;
                    if (action.hasPayload()) {
                        jo = HttpUtils.sendPostRequestWithParams(topeClient.getURL(actionStr), action.getPayload().getParameters());
                    } else {
                        jo = HttpUtils.sendGetRequest(topeClient.getURL(actionStr));
                    }

                    if (null != jo) {
                        JSONUtils ju = new JSONUtils(jo);
                        try {
                            boolean statusOk = ju.readAttr(JSON_RES_SUCCESS).equals(STR_TRUE);
                            cleanRun &= statusOk;
                            statusOk = ju.readAttr(JSON_RES_STATUS_CODE).equals(HttpUtils.STATUS_CODE_SUCCESS);
                            cleanRun &= statusOk;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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

    public static void printSuccessMsg(ITopeAction action, boolean successful, Activity activity){
        if (successful) {
            Toast.makeText(activity, "Successful: " + action.getTitle(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Failed: " + action.getTitle(), Toast.LENGTH_LONG).show();
        }
    }
}

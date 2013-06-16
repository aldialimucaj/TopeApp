package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.model.responses.ActionSynchResponse;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

public class ActionSynchExecutor extends MainExecutor<TopeResponse<ActionSynchResponse>> implements ITopeExecutable {

    private static final String TAG = "al.aldi.tope.controller.executables.ActionSynchExecutor";

    private TopeClient          client;
    private Context             context;

    public ActionSynchExecutor(ITopeAction action, Context context) {
        super(action, null);
        this.context = context;
    }

    @Override
    public void postRun(Object response) {

        @SuppressWarnings("unchecked")
        ActionSynchResponse actions = (ActionSynchResponse) ((TopeResponse<ActionSynchResponse>) response).getPayload();

        /* ***************************** ADDING ACTIONS TO DB ********************************************** */
        if (null != actions) {
            List<TopeAction> actionsList = actions.getActions();
            for (Iterator<TopeAction> iterator = actionsList.iterator(); iterator.hasNext();) {
                TopeAction topeAction = (TopeAction) iterator.next();
                topeAction.setClientId(client.getId());
            }

            ActionDataSource dataSource = new ActionDataSource(context);
            //dataSource.dropTable();// TODO: Dont drop the table. just delete all from the client and add the new ones
            dataSource.createTable();
            dataSource.delete((int) client.getId());
            dataSource.addAll(actionsList);
            // List<TopeAction> actionsList2 = dataSource.getAll();
        } else {
            Log.e(TAG, "Response payload in postRun is null");
        }
    }

    public TopeClient getClient() {
        return client;
    }

    public void setClient(TopeClient client) {
        this.client = client;
    }

    @Override
    public TopeResponse<ActionSynchResponse> convertResponse(String jsonString) {
        Type responseType = new TypeToken<TopeResponse<ActionSynchResponse>>() {
        }.getType();
        TopeResponse<ActionSynchResponse> tr = gson.fromJson(jsonString, responseType);

        return tr;
    }

    @Override
    public void setAction(ITopeAction action) {
        this.action = action;
    }

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void preRun(Object response) {
        // TODO Auto-generated method stub

    }

}

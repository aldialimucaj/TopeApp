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

import com.google.gson.reflect.TypeToken;

public class ActionSynchExecutor extends MainExecutor<TopeResponse<ActionSynchResponse>> implements ITopeExecutable<TopeResponse<ActionSynchResponse>> {

    private TopeClient client;
    private Context    context;

    public ActionSynchExecutor(ITopeAction<TopeResponse<ActionSynchResponse>> action, Context context) {
        super(action, null);
        this.context = context;
    }

    @Override
    public void postRun(TopeResponse<ActionSynchResponse> response) {
        System.out.println(response);
        ActionSynchResponse actions = response.getPayload();
        System.out.println(actions);

        /* ***************************** ADDING ACTIONS TO DB ********************************************** */

        List<TopeAction> actionsList = actions.getActions();
        for (Iterator iterator = actionsList.iterator(); iterator.hasNext();) {
            TopeAction topeAction = (TopeAction) iterator.next();
            topeAction.setClientId(client.getId());
        }

        ActionDataSource dataSource = new ActionDataSource(context);
        dataSource.dropTable();
        dataSource.createTable();
        dataSource.addAll(actionsList);
        List<TopeAction> actionsList2 = dataSource.getAll();

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

}

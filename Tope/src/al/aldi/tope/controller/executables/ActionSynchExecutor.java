package al.aldi.tope.controller.executables;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.model.responses.ActionSynchResponse;
import al.aldi.tope.utils.TopeSynchUtils;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * Action Executor for synchronizing server with client.
 */
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
        TopeSynchUtils tsu = new TopeSynchUtils();

        @SuppressWarnings("unchecked")
        TopeResponse<ActionSynchResponse> topeResponse = ((TopeResponse<ActionSynchResponse>) response);
        ActionSynchResponse actionsSyncResponse = (ActionSynchResponse) topeResponse.getPayload();
        

        /* ***************************** ADDING ACTIONS TO DB ********************************************** */
        if (null != actionsSyncResponse) {
            
            ClientDataSource cds = new ClientDataSource(context);
            client.setMac(actionsSyncResponse.getMac());
            cds.open();
            cds.updateClient(client);
            cds.close();
            
            List<TopeAction> actionsList = actionsSyncResponse.getActions();
            for (Iterator<TopeAction> iterator = actionsList.iterator(); iterator.hasNext();) {
                TopeAction topeAction = (TopeAction) iterator.next();
                topeAction.setTsu(tsu);
                topeAction.setClientId(client.getId());
                // setting the title
                String actionTitle = "";
                int title = tsu.getTitle(topeAction.getCommandFullPath());
                int textId = title == 0 ? R.string.tag_other_empty_string : title;
                try {
                    actionTitle = context.getString(textId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                topeAction.setTitle(actionTitle);
                // finished with the title 
                
                topeAction.setItemId(tsu.getIcon(topeAction.getCommandFullPath()));
                topeAction.setActive(!tsu.isIgnored(topeAction.getCommandFullPath()));
                topeAction.setConfirmationNeeded(tsu.isConfirmationNeeded(topeAction.getCommandFullPath()));
            }

            ActionDataSource dataSource = new ActionDataSource(context);
            dataSource.createTable();
            dataSource.delete((int) client.getId());
            dataSource.addAll(actionsList);
            dataSource.close();
            //List<TopeAction> actionsList2 = dataSource.getAll();
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
    public boolean preRun(Object response) {
        return true;
    }

}

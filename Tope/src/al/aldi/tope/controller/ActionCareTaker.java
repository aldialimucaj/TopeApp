package al.aldi.tope.controller;

import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.JsonTopeResponse;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.utils.TopeUtils;
import android.app.Activity;
import android.content.Context;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * It takes care of the HTTP response. It runs is execution function under
 * a separate thread.
 *
 * @author Aldi Alimucaj
 */
public class ActionCareTaker extends Thread {
    ITopeAction      action;
    Context          context;
    JsonTopeResponse response;
    ClientDataSource source;
    ActionDataSource actionSource;
    boolean successful = true;

    /**
     * Main Constructor
     *
     * @param action
     * @param activity
     * @param response
     */
    public ActionCareTaker(ITopeAction action, Activity activity, JsonTopeResponse response) {
        super();
        this.action = action;
        this.context = activity.getApplicationContext();
        this.response = response;
        source = TopeUtils.getClientDataSource(context);
        actionSource = TopeUtils.getActionDataSource(context);
    }

    public ActionCareTaker(ITopeAction action, Activity activity) {
        super();
        this.action = action;
        this.context = activity.getApplicationContext();
        source = TopeUtils.getClientDataSource(context);
        actionSource = TopeUtils.getActionDataSource(context);
    }

    public ActionCareTaker(ITopeAction action, Context context) {
        super();
        this.action = action;
        this.context = context;
        source = TopeUtils.getClientDataSource(context);
        actionSource = TopeUtils.getActionDataSource(context);
    }

    /**
     * The new thread function which iterates over the active clients and executes
     * the command on all of them. It also print a message about the outcome.
     */
    public void run() {

        Looper.prepare();
        if (null != action) {
            @SuppressWarnings("rawtypes")
            List<TopeResponse> topeResponses = new ArrayList<TopeResponse>();
            List<TopeClient> clients = source.getAllActive(action.getMethod()); /* reads all acitve clients from the database */
            //List<TopeClient> clients = source.getAllActive();/* reads all acitve clients from the database */
            for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext(); ) {
                TopeClient topeClient = (TopeClient) iterator.next();
                @SuppressWarnings("rawtypes")
                TopeResponse topeResponse = (TopeResponse) action.execute(topeClient);
                successful &= topeResponse.isSuccessful();
                topeResponses.add(topeResponse);
            }

            // clearing the payload, as it is to be reset every time.
            action.getPayload().clear();
            TopeUtils.printBulkSuccessMsg(topeResponses, action, context);
        } else {
            TopeUtils.printMsg(context, "Error: Action is null!");
        }
        Looper.loop();
    }

    /**
     * Executes the Tope command
     */
    public void execute() {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonTopeResponse getResponse() {
        return response;
    }

    public void setResponse(JsonTopeResponse response) {
        this.response = response;
    }

}

package al.aldi.tope.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.utils.TopeUtils;
import android.app.Activity;
import android.os.Looper;

/**
 * It takes care of the HTTP response. It runs is execution function under
 * a separate thread.
 *
 * @author Aldi Alimucaj
 *
 */
public class ActionCareTaker extends Thread {
    ITopeAction			action;
    Activity			activity;
    TopeResponse		response;
    ClientDataSource	source;
    boolean				successful	= true;

    /**
     * Main Constructor
     *
     * @param action
     * @param activity
     * @param response
     */
    public ActionCareTaker(ITopeAction action, Activity activity, TopeResponse response) {
        super();
        this.action = action;
        this.activity = activity;
        this.response = response;
        source = new ClientDataSource(activity.getApplicationContext());
    }

    public ActionCareTaker(ITopeAction action, Activity activity) {
        super();
        this.action = action;
        this.activity = activity;
        source = new ClientDataSource(activity.getApplicationContext());
    }

    /**
     * The new thread function which iterates over the active clients and executes
     * the command on all of them. It also print a message about the outcome.
     */
    public void run() {

        Looper.prepare();
        if (null != action) {
            source.open();
            List<TopeResponse> topeResponses = new ArrayList<TopeResponse>();
            List<TopeClient> clients = source.getAllActive(); /* reads all acitve clients from the database */
            for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext();) {
                TopeClient topeClient = (TopeClient) iterator.next();
                TopeResponse topeResponse = action.execute(topeClient);
                successful &= topeResponse.isSuccessful();
                topeResponses.add(topeResponse);
            }

            TopeUtils.printBulkSuccessMsg(topeResponses, action, activity);
        } else{
            TopeUtils.printMsg(activity, "Error: Action is null!");
        }
        Looper.loop();
    }

    /**
     * Executes the Tope command
     */
    public void execute() {
        start();
    }

    public TopeResponse getResponse() {
        return response;
    }

    public void setResponse(TopeResponse response) {
        this.response = response;
    }

}

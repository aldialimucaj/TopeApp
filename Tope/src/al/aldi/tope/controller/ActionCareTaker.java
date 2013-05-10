package al.aldi.tope.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import al.aldi.tope.TopeUtils;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ClientDataSource;
import android.app.Activity;
import android.os.Looper;

public class ActionCareTaker extends Thread {
    ITopeAction			action;
    Activity			activity;
    TopeResponse		response;
    ClientDataSource	source;
    boolean				successful	= true;

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

    public void run() {

        Looper.prepare();

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
        Looper.loop();
    }

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

package al.aldi.tope.controller;

import al.aldi.tope.TopeUtils;
import android.app.Activity;
import android.os.Looper;

public class ActionCareTaker extends Thread {
    ITopeAction action;
    Activity activity;


    public ActionCareTaker(ITopeAction action, Activity activity) {
        super();
        this.action = action;
        this.activity = activity;
    }


    public void run() {
          Looper.prepare();
          boolean successful = action.execute();
          TopeUtils.printSuccessMsg(action, successful, activity);
          Looper.loop();
    }


    public void execute() {
        start();
    }
}

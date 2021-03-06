package al.aldi.tope.controller.executables;

import al.aldi.libjaldi.net.NetUtils;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.model.responses.TestResponse;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.Socket;

public class WakeOnLanExecutor extends MainExecutor<TopeResponse<TestResponse>> implements ITopeExecutable {
    public static final String TAG = "Tope.WakeOnLanExecutor";

    Context     context  = null;
    ITopeAction action   = null;
    Fragment    fragment = null;

    public WakeOnLanExecutor() {
        super(null, null);
    }

    public WakeOnLanExecutor(Fragment fragment) {
        super(null, fragment);
    }

    public WakeOnLanExecutor(ITopeAction testAction, Fragment fragment) {
        super(testAction, fragment);
        this.action = testAction;
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(testAction, fragment));
    }

    @Override
    public Object run(TopeClient topeClient) {
        NetworkComm nc = new NetworkComm(topeClient.getIp(), topeClient.getMac());
        nc.doInBackground("");

        return new TopeResponse<EmptyResponse>(true, true);
    }

    @Override
    public void postRun(Object response) {
        // i might want to check if the pc turned on after 10 secs or so...
    }

    @Override
    public TopeResponse<TestResponse> convertResponse(String jsonString) {
        Type responseType = new TypeToken<TopeResponse<TestResponse>>() {
        }.getType();
        TopeResponse<TestResponse> tr = gson.fromJson(jsonString, responseType);
        return tr;
    }

    @Override
    public void setAction(ITopeAction action) {
        super.setAction(action);
        this.action = action;
    }

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(action, fragment));
    }

    @Override
    public boolean preRun(Object response) {
        return true;
    }

    static class NetworkComm extends AsyncTask<String, Integer, Socket> {

        String ip;
        String mac;
        int port = 9;

        public NetworkComm(String ip, String mac) {
            this.ip = ip;
            this.mac = mac;
        }

        @Override
        protected Socket doInBackground(String... params) {
            try {
                NetUtils.wol(mac);
                Log.i(TAG, "Wake-on-LAN packet sent.");
            } catch (Exception e) {
                Log.i(TAG, "Failed to send Wake-on-LAN packet: " + e);
                e.printStackTrace();
            }
            return null;
        }


    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}

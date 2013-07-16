package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;
import java.util.Vector;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.model.responses.SimpleTextResponse;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

public class ClipboardReadPayloadExecutor extends MainExecutor<TopeResponse<SimpleTextResponse>> implements ITopeExecutable {

    private static final String TAG          = "al.aldi.tope.controller.executables.TestExecutable";

    ITopeAction                 action       = null;

    public ClipboardReadPayloadExecutor() {
        super(null, null);
    }

    public ClipboardReadPayloadExecutor(Fragment fragment) {
        super(null, fragment);
    }

    public ClipboardReadPayloadExecutor(ITopeAction testAction, Fragment fragment) {
        super(testAction, fragment);
        this.action = testAction;
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(testAction, fragment));
    }

    @Override
    public void postRun(Object response) {
        if (null == response) {
            return;
        }
        
        //TODO: This can only work for one client
        @SuppressWarnings("rawtypes")
        SimpleTextResponse textResponse = (SimpleTextResponse) ((TopeResponse) response).getPayload();
        if (null != textResponse) {
            String msg = textResponse.getTestMessage();
            if (null != msg) {
                Log.i(TAG, msg);
                ClipboardManager cm = (ClipboardManager) fragment.getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", msg);
                cm.setPrimaryClip(clip); 
            }
        }
    }

    @Override
    public TopeResponse<SimpleTextResponse> convertResponse(String jsonString) {
        Type responseType = new TypeToken<TopeResponse<SimpleTextResponse>>() {
        }.getType();
        TopeResponse<SimpleTextResponse> tr = gson.fromJson(jsonString, responseType);
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
        ClientDataSource clientDataSource = new ClientDataSource(fragment.getActivity());
        clientDataSource.open();
        Vector<TopeClient> clients = clientDataSource.getAllActive();
        clientDataSource.close();
        
        if(clients.size() > 1){
            Toast.makeText(fragment.getActivity(), "You cannot copy from multiple clients!", Toast.LENGTH_LONG).show();
            return false;
        }
        
        return true;

    }

}

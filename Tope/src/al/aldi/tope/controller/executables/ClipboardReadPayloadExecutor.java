package al.aldi.tope.controller.executables;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.model.responses.SimpleTextResponse;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Vector;

import static android.util.Log.e;

public class ClipboardReadPayloadExecutor extends MainExecutor<TopeResponse<SimpleTextResponse>> implements ITopeExecutable {

public static final String TAG = "Tope.ClipboardReadPayloadExecutor";
    ITopeAction action  = null;
    Context     context = null;

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

        //This can only work for one client
        @SuppressWarnings("rawtypes")
        SimpleTextResponse textResponse = (SimpleTextResponse) ((TopeResponse) response).getPayload();
        if (null != textResponse) {
            String msg = textResponse.getTestMessage();
            Context t_context = null;
            if (null != fragment && null != fragment.getActivity()) {
                t_context = fragment.getActivity().getApplicationContext();
            } else if (null != context) {
                t_context = context;
            } else {
                e(TAG, "ClipboardWritePayloadExecutor.preRun(): Context and Frame is null cant read clipboard service");
            }

            if (null != msg && null != t_context) {
                Log.i(TAG, msg);
                ClipboardManager cm = (ClipboardManager) t_context.getSystemService(Activity.CLIPBOARD_SERVICE);
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
        ClientDataSource clientDataSource = TopeUtils.getClientDataSource(context);
        Vector<TopeClient> clients = clientDataSource.getAllActive();

        if (clients.size() > 1) {
            Toast.makeText(fragment.getActivity(), "You cannot copy from multiple clients!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

}

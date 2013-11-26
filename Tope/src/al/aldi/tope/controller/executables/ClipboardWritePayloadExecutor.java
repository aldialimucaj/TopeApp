package al.aldi.tope.controller.executables;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.util.Log.e;

public class ClipboardWritePayloadExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {
    public static final String TAG = "Tope.ClipboardWritePayloadExecutor";

    ITopeAction action  = null;
    Context     context = null;

    public ClipboardWritePayloadExecutor() {
        super(null, null);
    }

    public ClipboardWritePayloadExecutor(Fragment fragment) {
        super(null, fragment);
    }

    public ClipboardWritePayloadExecutor(ITopeAction testAction, Fragment fragment) {
        super(testAction, fragment);
        this.action = testAction;
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(testAction, fragment));
        this.payload = action.getPayload();
    }

    @Override
    public void postRun(Object response) {
        if (null == response) {
            return;
        }
    }

    @Override
    public TopeResponse<EmptyResponse> convertResponse(String jsonString) {
        Type responseType = new TypeToken<TopeResponse<EmptyResponse>>() {
        }.getType();
        TopeResponse<EmptyResponse> tr = gson.fromJson(jsonString, responseType);
        return tr;
    }

    @Override
    public void setAction(ITopeAction action) {
        super.setAction(action);
        this.action = action;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(action, fragment));
    }

    @Override
    public boolean preRun(Object payload) {
        if (action.getPayload() != payload) {
            Context t_context = null;
            if (null != fragment && null != fragment.getActivity()) {
                t_context = fragment.getActivity().getApplicationContext();
            } else if (null != context) {
                t_context = context;
            } else {
                e(TAG, "ClipboardWritePayloadExecutor.preRun(): Context and Frame is null cant read clipboard service");
                return false;
            }

            try {
                ClipboardManager cm = (ClipboardManager) t_context.getSystemService(Activity.CLIPBOARD_SERVICE);
                if(cm.hasPrimaryClip() && cm.getPrimaryClip().getItemCount() != 0) {
                    String androidClipboard = cm.getPrimaryClip().getItemAt(0).getText().toString();
                    ((ITopePayload) action.getPayload()).addPayload(TopePayload.PARAM_ARG_0, androidClipboard);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;

    }

}

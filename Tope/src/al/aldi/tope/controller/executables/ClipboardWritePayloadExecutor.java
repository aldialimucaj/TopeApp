package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.app.Activity;
import android.content.ClipboardManager;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;

public class ClipboardWritePayloadExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {

    ITopeAction                 action       = null;

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
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(action, fragment));
    }

    @Override
    public boolean preRun(Object payload) {
        if(action.getPayload() != payload) {
            ClipboardManager cm = (ClipboardManager) fragment.getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
            String androidClipboard = cm.getPrimaryClip().getItemAt(0).getText().toString();
            try {
                ((ITopePayload) action.getPayload()).addPayload(TopePayload.PARAM_ARG_0, androidClipboard);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;

    }

}

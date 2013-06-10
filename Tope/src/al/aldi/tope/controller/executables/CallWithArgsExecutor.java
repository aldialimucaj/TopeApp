/**
 *
 */
package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.view.dialog.fragment.CallWithTextActionDialog;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;

/**
 * @author Aldi Alimucaj
 *
 */
public class CallWithArgsExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {

    ITopeAction                 action       = null;
    TopeResponse<EmptyResponse> topeResponse = null;
    Fragment                    fragment     = null;
    ITopePayload                payload      = null;

    public CallWithArgsExecutor(ITopeAction defaultAction, Fragment fragment) {
        super(defaultAction, fragment);
        this.action = defaultAction;
        this.fragment = fragment;

        defaultAction.setContextView(new CallWithTextActionDialog(action, fragment));
    }

    public CallWithArgsExecutor(Fragment fragment) {
        super(null, fragment);
        this.fragment = fragment;
    }

    @Override
    public void preRun(Object payload) {

    }

    @Override
    public void postRun(Object response) {
        // TODO Auto-generated method stub

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
        this.action = action;
        super.setAction(action);
        action.setContextView(new CallWithTextActionDialog(action, fragment));
    }

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public ITopePayload getPayload() {
        return payload;
    }

    public void setPayload(ITopePayload payload) {
        this.payload = payload;
    }

}

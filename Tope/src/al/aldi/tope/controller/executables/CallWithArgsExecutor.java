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
import al.aldi.tope.utils.TopeCommands;
import al.aldi.tope.view.dialog.fragment.CallWithTextActionDialog;
import al.aldi.tope.view.dialog.fragment.IDialogFieldTypes;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;

/**
 * @author Aldi Alimucaj
 *
 */
public class CallWithArgsExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {

    TopeResponse<EmptyResponse> topeResponse = null;
    IDialogFieldTypes           fieldType    = null;

    CallWithTextActionDialog    dialog       = null;

    public CallWithArgsExecutor(ITopeAction defaultAction, Fragment fragment) {
        super(defaultAction, fragment);
        this.action = defaultAction;
        dialog = new CallWithTextActionDialog(action, fragment);
        defaultAction.setContextView(dialog);
        checkActionType();
    }



    public CallWithArgsExecutor(ITopeAction defaultAction, Fragment fragment, IDialogFieldTypes type) {
        this(defaultAction, fragment);
        this.fieldType = type;
    }

    public CallWithArgsExecutor(Fragment fragment) {
        super(null, fragment);
        this.fragment = fragment;
    }

    private void checkActionType() {
       if(null != action){
           if(action.getCommandFullPath().equals(TopeCommands.PROG_BROWSER_OPEN_URL)){
               setFieldType(IDialogFieldTypes.TEXT_URL);
           }
       }

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
        dialog = new CallWithTextActionDialog(action, fragment);
        action.setContextView(dialog);
        checkActionType();
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

    public IDialogFieldTypes getFieldType() {
        return fieldType;
    }

    public void setFieldType(IDialogFieldTypes fieldType) {
        this.fieldType = fieldType;
        if (null != dialog) {
            dialog.setFieldType(fieldType);
        }
    }

}

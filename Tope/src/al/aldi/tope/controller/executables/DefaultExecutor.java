/**
 *
 */
package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.model.responses.TestResponse;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;

/**
 * @author Aldi Alimucaj
 *
 */
public class DefaultExecutor extends MainExecutor<TopeResponse> implements ITopeExecutable<TopeResponse>  {

    ITopeAction<TopeResponse> action       = null;
    TopeResponse              topeResponse = null;
    Fragment                  fragment     = null;

    public DefaultExecutor(ITopeAction<TopeResponse> defaultAction, Fragment fragment) {
        super(defaultAction, fragment);
        this.action = defaultAction;
        this.fragment = fragment;
    }

    @Override
    public void postRun(TopeResponse response) {
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
    }
    @Override
    public void setFragment(Fragment fragment) {
       this.fragment = fragment;
    }


}

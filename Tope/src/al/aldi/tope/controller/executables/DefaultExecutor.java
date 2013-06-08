/**
 *
 */
package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;

import al.aldi.tope.model.ITopeAction;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;

/**
 * @author Aldi Alimucaj
 *
 */
public class DefaultExecutor<TopeResponse> extends MainExecutor<TopeResponse> {

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
    public TopeResponse convertResponse(String jsonString) {
        Type responseType = new TypeToken<TopeResponse>() {
        }.getType();
        TopeResponse tr = gson.fromJson(jsonString, responseType);
        return tr;
    }

}

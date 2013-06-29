/**
 *
 */
package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;

import com.google.gson.reflect.TypeToken;

/**
 * @author Aldi Alimucaj
 * 
 */
public class PingExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {

    ITopeAction                 action        = null;
    TopeResponse<EmptyResponse> topeResponse  = null;
    PagerTabStrip               pagerTabStrip = null;

    public PingExecutor(ITopeAction defaultAction, PagerTabStrip pagerTabStrip) {
        super(defaultAction, null);
        this.action = defaultAction;
        this.pagerTabStrip = pagerTabStrip;
    }

    @Override
    public void postRun(Object response) {
        
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

    @Override
    public boolean preRun(Object response) {
        return true;
    }

}

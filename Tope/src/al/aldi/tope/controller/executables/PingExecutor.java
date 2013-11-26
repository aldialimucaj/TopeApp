/**
 *
 */
package al.aldi.tope.controller.executables;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.utils.TopeHttpUtil;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @author Aldi Alimucaj
 * 
 */
public class PingExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {

    ITopeAction                 action        = null;
    TopeResponse<EmptyResponse> topeResponse  = null;
    PagerTabStrip               pagerTabStrip = null;
    Context                     context       = null;

    public PingExecutor(ITopeAction defaultAction, PagerTabStrip pagerTabStrip) {
        super(defaultAction, null);
        this.action = defaultAction;
        this.pagerTabStrip = pagerTabStrip;
    }

    @Override
    public Object run(TopeClient topeClient) {

        /* ******************************************************************************************** */

        boolean continueExecution = preRun(payload);

        /* ******************************************************************************************** */
        if (continueExecution) {
            try {

                action.getPayload().addPayload(TopePayload.PARAM_USER, topeClient.getUser());
                action.getPayload().addPayload(TopePayload.PARAM_PASSWORD, topeClient.getPass());
                action.getPayload().addPayload(TopePayload.PARAM_DOMAIN, topeClient.getDomain());
                action.getPayload().addPayload(TopePayload.PARAM_METHOD, action.getMethod());
                action.getPayload().addPayload(TopePayload.PARAM_ACTION_ID, String.valueOf(action.getActionId()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String responseString = new TopeHttpUtil<TopeResponse<EmptyResponse>>(2000, 2000).sendPostRequestWithParamsRetString(topeClient.getSslURL(action.getCommandFullPath()), action.getPayload().getParameters());

            /* ******************** CALLING ABSTRACT METHOD TO TAKE CARE OF THE OUTPUT ******************** */
            topeResponse = convertResponse(responseString);

            postRun(topeResponse);

            /* ******************************************************************************************** */
        } else {
            topeResponse = convertResponse("{}");
        }
        return topeResponse;
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

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

}

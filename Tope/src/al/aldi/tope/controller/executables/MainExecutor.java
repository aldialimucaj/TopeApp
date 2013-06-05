package al.aldi.tope.controller.executables;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.utils.TopeHttpUtil;
import android.support.v4.app.Fragment;

public abstract class MainExecutor<E> implements ITopeExecutable<E> {

    ITopeAction<E> action       = null;
    E              topeResponse = null;
    Fragment       fragment     = null;

    public MainExecutor(ITopeAction<E> action, Fragment fragment) {
        this.action = action;
        this.fragment = fragment;
    }

    @Override
    public E run(TopeClient topeClient) {
        try {

            action.getPayload().addPayload(TopePayload.PARAM_USER, topeClient.getUser());
            action.getPayload().addPayload(TopePayload.PARAM_PASSWORD, topeClient.getPass());
            action.getPayload().addPayload(TopePayload.PARAM_DOMAIN, topeClient.getDomain());
            action.getPayload().addPayload(TopePayload.PARAM_ACTION_ID, String.valueOf(action.getActionId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        topeResponse = new TopeHttpUtil<E>().sendPostRequestWithParams(topeClient.getSslURL(action.getCommand()), action.getPayload().getParameters());

        /* ******************** CALLING ABSTRACT METHOD TO TAKE CARE OF THE OUTPUT ******************** */
        
        postRun(topeResponse);
        
        /* ******************************************************************************************** */

        return topeResponse;
    }

    public abstract void postRun(E response);

}

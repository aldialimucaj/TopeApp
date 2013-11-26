package al.aldi.tope.controller.executables;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.view.activities.ShortcutsActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Executor for Action <b>Shortcuts</b>
 */
public class ShortcutsExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {

    Context context = null;


    public ShortcutsExecutor(Fragment fragment) {
        super(null, fragment);

    }

    public ShortcutsExecutor(ITopeAction action, Fragment fragment) {
        super(action, fragment);

    }

    @Override
    public Object run(TopeClient topeClient) {
        fragment.startActivity(new Intent(fragment.getActivity(), ShortcutsActivity.class));

        return new TopeResponse<EmptyResponse>(true, false);
    }

    @Override
    public TopeResponse<EmptyResponse> convertResponse(String jsonString) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean preRun(Object response) {
        return true;
    }


    @Override
    public void postRun(Object response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}

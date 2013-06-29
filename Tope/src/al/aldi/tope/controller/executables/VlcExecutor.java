package al.aldi.tope.controller.executables;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import al.aldi.tope.view.VlcControlActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class VlcExecutor extends MainExecutor<TopeResponse<EmptyResponse>> implements ITopeExecutable {

    
    public VlcExecutor(Fragment fragment) {
        super(null, fragment);
        
    }
    
    public VlcExecutor(ITopeAction action, Fragment fragment) {
        super(action, fragment);
        
    }
    
    @Override
    public Object run(TopeClient topeClient) {
        fragment.startActivity(new Intent(fragment.getActivity(), VlcControlActivity.class));
        
        return new TopeResponse<EmptyResponse>();
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

}

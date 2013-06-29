package al.aldi.tope.controller.executables;

import java.lang.reflect.Type;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.TestResponse;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

public class TestExecutor extends MainExecutor<TopeResponse<TestResponse>> implements ITopeExecutable {

    private static final String TAG          = "al.aldi.tope.controller.executables.TestExecutable";

    ITopeAction                 action       = null;
    Fragment                    fragment     = null;

    public TestExecutor() {
        super(null, null);
    }

    public TestExecutor(Fragment fragment) {
        super(null, fragment);
    }

    public TestExecutor(ITopeAction testAction, Fragment fragment) {
        super(testAction, fragment);
        this.action = testAction;
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(testAction, fragment));
    }

    @Override
    public void postRun(Object response) {
        if (null == response) {
            return;
        }

        @SuppressWarnings("rawtypes")
        TestResponse testResponse = (TestResponse) ((TopeResponse) response).getPayload();
        if (null != testResponse) {
            String testMsg = testResponse.getTestMessage();
            if (null != testMsg) {
                Log.i(TAG, testMsg);
            }
        }
    }

    @Override
    public TopeResponse<TestResponse> convertResponse(String jsonString) {
        Type responseType = new TypeToken<TopeResponse<TestResponse>>() {
        }.getType();
        TopeResponse<TestResponse> tr = gson.fromJson(jsonString, responseType);
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
    public boolean preRun(Object response) {
        return true;
    }


}

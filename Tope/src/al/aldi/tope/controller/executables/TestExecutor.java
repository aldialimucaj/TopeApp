package al.aldi.tope.controller.executables;

import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.TestResponse;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.support.v4.app.Fragment;
import android.util.Log;

public class TestExecutor extends MainExecutor<TopeResponse<TestResponse>> implements ITopeExecutable<TopeResponse<TestResponse>> {

    private static final String             TAG          = "al.aldi.tope.controller.executables.TestExecutable";

    ITopeAction<TopeResponse<TestResponse>> action       = null;
    TopeResponse<TestResponse>              topeResponse = null;
    Fragment                                fragment     = null;

    public TestExecutor(ITopeAction<TopeResponse<TestResponse>> testAction, Fragment fragment) {
        super(testAction, fragment);
        this.action = testAction;
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(testAction, fragment));
    }

    @Override
    public void postRun(TopeResponse<TestResponse> response) {
        if (null == response) {
            return;
        }

        TestResponse testResponse = response.getPayload();
        if (null != testResponse) {
            String testMsg = testResponse.getTestMessage();
            if (null != testMsg) {
                Log.i(TAG, testMsg);
            }
        }
    }

}

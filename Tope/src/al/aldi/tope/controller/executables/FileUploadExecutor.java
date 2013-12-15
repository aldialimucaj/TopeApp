package al.aldi.tope.controller.executables;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopePayload;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.TestResponse;
import al.aldi.tope.view.dialog.fragment.StandardActionDialog1;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileUploadExecutor extends MainExecutor<TopeResponse<TestResponse>> implements ITopeExecutable {

    private static final String TAG = "al.aldi.tope.controller.executables.FileUploadExecutor";
    Context context = null;

    ITopeAction action   = null;
    Fragment    fragment = null;
    List<File>  files    = new ArrayList<File>();

    public FileUploadExecutor() {
        super(null, null);
    }

    public FileUploadExecutor(Fragment fragment) {
        super(null, fragment);
    }

    public FileUploadExecutor(ITopeAction testAction, Fragment fragment) {
        super(testAction, fragment);
        this.action = testAction;
        this.fragment = fragment;
        this.action.setContextView(new StandardActionDialog1(testAction, fragment));
    }

    public FileUploadExecutor(List<Uri> uriFiles) {
        this();
        for (Uri uri : uriFiles) {
            files.add(new File(uri.getPath()));
        }
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                action.getPayload().addPayload(TopePayload.PARAM_ARG_0, String.valueOf(sharedPrefs.getBoolean("upload_overwrite_file", false)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (File file : files) {
                try {
                    topeResponse = convertResponse(new HttpUtils().uploadFile(topeClient.getSslURL(action.getCommandFullPath()), file, action.getPayload().getParameters()));
                    if(!topeResponse.isSuccessful())
                        break;
                } catch (JsonSyntaxException e) {
                    topeResponse = new TopeResponse<TestResponse>(false);
                }
            }

        } else {
            topeResponse = convertResponse("{}");
        }
        return topeResponse;
    }

    @Override
    public void postRun(Object response) {
    }

    @Override
    public TopeResponse<TestResponse> convertResponse(String jsonString) throws JsonSyntaxException {
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

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}

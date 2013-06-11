package al.aldi.tope.view.dialog.fragment;

import al.aldi.tope.R;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.ITopePayload;
import al.aldi.tope.model.TopePayload;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Standard Action Dialog which offers an execution date and a timer for the action.
 *
 * @author Aldi Alimucaj
 *
 */
public class CallWithTextActionDialog extends LinearLayout implements ITopeActionDialog {
    protected static final String TAG          = "CallWithTextActionDialog";
    LinearLayout.LayoutParams     layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
    EditText                      editText1    = null;
    IDialogFieldTypes             fieldType    = null;
    ITopeAction                   action       = null;

    public CallWithTextActionDialog(Context context) {
        super(context);
    }

    public CallWithTextActionDialog(final ITopeAction action, final Fragment fragment) {
        super(fragment.getActivity());
        this.action = action;
        layoutParams.setMargins(10, 0, 10, 0);

        LayoutInflater li = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) li.inflate(R.layout.dialog_with_text1, this, false);

        editText1 = (EditText) ll.findViewById(R.id.dialog_name_editText1);

        addView(ll);

    }

    public CallWithTextActionDialog(final ITopeAction action, final Fragment fragment, IDialogFieldTypes type) {
        this(action, fragment);
        this.fieldType = type;
    }

    @Override
    public void setUp() {
        ITopePayload payload = action.getPayload();
        try {
            payload.addPayload(TopePayload.PARAM_ARG_0, getText1Value());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getText1Value() {
        String httpPrefix = "http://";
        String httpsPrefix = "https://";
        String returnText = "";
        String editTxt = editText1.getText().toString();
        if (fieldType == IDialogFieldTypes.TEXT_URL && !editTxt.startsWith(httpPrefix) && !editTxt.startsWith(httpsPrefix)) {
            returnText += httpPrefix + editText1.getText().toString();
            return returnText;
        }
        return editText1.getText().toString();
    }

    @Override
    public void cleanUp() {
        // editText1.setText("");
    }

    public IDialogFieldTypes getFieldType() {
        return fieldType;
    }

    public void setFieldType(IDialogFieldTypes fieldType) {
        this.fieldType = fieldType;
    }

}

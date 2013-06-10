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

    public CallWithTextActionDialog(Context context) {
        super(context);
    }

    public CallWithTextActionDialog(final ITopeAction action, final Fragment fragment) {
        super(fragment.getActivity());
        layoutParams.setMargins(10, 0, 10, 0);

        LayoutInflater li = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) li.inflate(R.layout.dialog_with_text1, this, false);

        editText1 = (EditText) ll.findViewById(R.id.dialog_name_editText1);

        editText1.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                ITopePayload payload = action.getPayload();
                try {
                    payload.addPayload(TopePayload.PARAM_ARG_0, getText1Value());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        addView(ll);

    }

    public String getText1Value(){
        return editText1.getText().toString();
    }

    @Override
    public void cleanUp() {
        //editText1.setText("");
    }
}

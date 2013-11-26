package al.aldi.tope.view.activities;

import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.utils.TopeActionUtils;
import al.aldi.tope.view.widgets.TopeAppWidgetProvider;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * User: Aldi Alimucaj
 * Date: 24.11.13
 * Time: 21:32
 * <p/>
 * Activity dialog to be able to ask for confirmation on widget interaction.
 */
public class WidgetDialogActivity extends Activity {
    ITopeAction action = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_dialog);
        action = getIntent().getExtras().getParcelable(TopeAppWidgetProvider.WidgetUtils.TOPE_ACTION);
        initListeners();
    }

    private void initListeners() {
        TextView title = (TextView) findViewById(R.id.widgetDialogActivity_title);
        title.setText(action.getTitle()+"?");
        Button yes = (Button) findViewById(R.id.widgetDialogActivity_ButtonYes);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ITopeAction topeAction = action;
                ITopeExecutable executable = TopeActionUtils.TopeActionUtilsManager.getExecutor(action.getCommandFullPath());
                if (null == executable) {
                    executable = new CallWithArgsExecutor(topeAction, getApplicationContext());
                }
                topeAction.setExecutable(executable);
                ActionCareTaker act = new ActionCareTaker(topeAction, WidgetDialogActivity.this);
                act.execute();
                WidgetDialogActivity.this.finish();
            }
        });
        Button no = (Button) findViewById(R.id.widgetDialogActivity_ButtonNo);
        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WidgetDialogActivity.this.finish();
            }
        });
    }
}

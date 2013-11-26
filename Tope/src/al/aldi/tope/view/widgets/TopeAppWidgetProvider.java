package al.aldi.tope.view.widgets;

import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.utils.TopeActionUtils;
import al.aldi.tope.utils.TopeCommands;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.activities.WidgetDialogActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import static android.util.Log.i;

/**
 * User: Aldi Alimucaj
 * Date: 24.11.13
 * Time: 14:06
 * <p/>
 * Tope App Widget with most common actions
 */
public class TopeAppWidgetProvider extends AppWidgetProvider {
    public static final  String TAG        = "Tope.TopeAppWidgetProvider";
    private static final String URI_SCHEME = "TOPE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int widgetsLenght = appWidgetIds.length;
        i(TAG, "TopeAppWidgetProvider.onUpdate(): ");

        // there is only one widget for tope yet but we will stick with the loop
        for (int i = 0; i < widgetsLenght; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_tope);

            // Adding logic to widget actions
            Intent intentHibernate = new Intent();
            intentHibernate.setAction(WidgetUtils.WIDGET_EXECUTE_ACTION);
            intentHibernate.putExtra(WidgetUtils.TOPE_ACTION, TopeCommands.OS_STAND_BY);
            views.setOnClickPendingIntent(R.id.gridActionImageStandby, PendingIntent.getBroadcast(context, 1, intentHibernate, PendingIntent.FLAG_UPDATE_CURRENT));

            Intent intentWoL = new Intent();
            intentWoL.setAction(WidgetUtils.WIDGET_EXECUTE_ACTION);
            intentWoL.putExtra(WidgetUtils.TOPE_ACTION, TopeCommands.OS_WAKE_ON_LAN);
            views.setOnClickPendingIntent(R.id.gridActionImageWoL, PendingIntent.getBroadcast(context, 2, intentWoL, PendingIntent.FLAG_UPDATE_CURRENT));

            Intent intentCopy = new Intent();
            intentCopy.setAction(WidgetUtils.WIDGET_EXECUTE_ACTION);
            intentCopy.putExtra(WidgetUtils.TOPE_ACTION, TopeCommands.UTIL_READ_CLIPBOARD);
            views.setOnClickPendingIntent(R.id.gridActionImageCopy, PendingIntent.getBroadcast(context, 3, intentCopy, PendingIntent.FLAG_UPDATE_CURRENT));

            Intent intentPaste = new Intent();
            intentPaste.setAction(WidgetUtils.WIDGET_EXECUTE_ACTION);
            intentPaste.putExtra(WidgetUtils.TOPE_ACTION, TopeCommands.UTIL_WRITE_CLIPBOARD);
            views.setOnClickPendingIntent(R.id.gridActionImagePaste, PendingIntent.getBroadcast(context, 4, intentPaste, PendingIntent.FLAG_UPDATE_CURRENT));

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "TopeAppWidgetProvider.onReceive(): ");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), TopeAppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        if (intent.getAction().equals(WidgetUtils.WIDGET_EXECUTE_ACTION)) {
            final String actionName = intent.getStringExtra(WidgetUtils.TOPE_ACTION);
            ActionDataSource actionDataSource = TopeUtils.getActionDataSource(context);
            ITopeAction action = actionDataSource.getAction(actionName);
            Log.i(TAG, "TopeAppWidgetProvider.onReceive(): action=" + action);
            if (null != action) {
                ITopeExecutable executable = TopeActionUtils.TopeActionUtilsManager.getExecutor(action.getCommandFullPath());
                if (null == executable) {
                    executable = new CallWithArgsExecutor(action, context);
                }
                executable.setContext(context);
                action.setExecutable(executable);
                // this will execute to all active clients
                askAndExecute(action, context);
            }
        }

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

    }

    private void executeAction(ITopeAction action, Context context) {

        ActionCareTaker act = null;
        try {
            act = new ActionCareTaker(action, context);
            act.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void askAndExecute(final ITopeAction action, final Context context) {
        if (action.isConfirmationNeeded()) {
            Intent i = new Intent(context, WidgetDialogActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(WidgetUtils.TOPE_ACTION, action);
            context.startActivity(i);
        } else {
            executeAction(action, context);
        }
    }

    public static class WidgetUtils {
        public static final String WIDGET_EXECUTE_ACTION = "al.aldi.tope.action.WIDGET_EXECUTE_ACTION";
        public final static String TOPE_ACTION           = "TOPE_ACTION";

    }

}

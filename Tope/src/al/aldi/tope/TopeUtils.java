package al.aldi.tope;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.SettingsMgr;
import al.aldi.tope.controller.TopeAction;
import android.content.Context;

public class TopeUtils {

    public static ITopeAction addAction(Context context, final String actionStr, int itemId) {
        ITopeAction action = new TopeAction(context, itemId);
        action.setExecutable(new ITopeExecutable() {
            @Override
            public boolean run() {
                final SettingsMgr sMgr = SettingsMgr.getInstance();
                return HttpUtils.sendGetRequest(sMgr.getURL(actionStr));
            }
        });

        return action;
    }
}

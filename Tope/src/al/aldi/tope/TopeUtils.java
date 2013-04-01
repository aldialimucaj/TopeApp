package al.aldi.tope;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.SettingsMgr;
import al.aldi.tope.controller.TopeAction;

public class TopeUtils {

    public static ITopeAction addAction(final String actionStr, int itemId, String title) {
        ITopeAction action = new TopeAction(itemId, title);
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

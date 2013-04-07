package al.aldi.tope;

import java.util.Iterator;
import java.util.List;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.db.ClientDataSource;

public class TopeUtils {
    public static final String	TOPE_DEFAULT_PORT	= "8080";

    ClientDataSource			source				= null;

    public TopeUtils(ClientDataSource source) {
        super();
        this.source = source;
    }

    public ITopeAction addAction(final String actionStr, int itemId, String title) {
        ITopeAction action = new TopeAction(itemId, title);
        action.setExecutable(new ITopeExecutable() {
            @Override
            public boolean run() {
                boolean cleanRun = true;
                source.open();
                List<TopeClient> clients = source.getAllActive();
                for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext();) {
                    TopeClient topeClient = (TopeClient) iterator.next();
                    cleanRun &= HttpUtils.sendGetRequest(topeClient.getURL(actionStr));
                }
                return cleanRun;
            }
        });

        return action;
    }

}

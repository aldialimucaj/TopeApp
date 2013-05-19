package al.aldi.tope.view.listeners;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;

public class ActionContextMenueListener implements OnCreateContextMenuListener {

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
          menu.setHeaderTitle("Context Menu");
          menu.add(0, v.getId(), 0, "Add parameters");

    }

}

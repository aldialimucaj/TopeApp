package al.aldi.tope.view.adapter;

import java.util.HashMap;

import al.aldi.tope.controller.ITopeAction;
import android.view.View;

public interface ITopeLongClickAdapter {
    public HashMap<ITopeAction, View> getTopeActionViewMap();

    public void setTopeActionViewMap(HashMap<ITopeAction, View> topeActionViewMap);
}

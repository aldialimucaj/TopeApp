package al.aldi.tope.view.adapter;

import java.util.HashMap;
import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.TopeUtils;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.view.listeners.ActionTouchListener;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconItemAdapter<E> extends BaseAdapter implements ITopeLongClickAdapter {

    public static final int			WIDTH_160				= 160;
    public static final int			HEIGHT_250				= 250;
    public static final int			HEIGHT_230				= 230;
    Vector<ITopeAction>				actions;
    private Activity				activity;

    HashMap<ITopeAction, View>		topeActionViewMap		= new HashMap<ITopeAction, View>();
    HashMap<ITopeAction, Object>	topeActionCommandMap	= new HashMap<ITopeAction, Object>();

    public IconItemAdapter() {
        super();
    }

    public IconItemAdapter(Activity activity, Vector<ITopeAction> itmes) {
        this.activity = activity;
        this.actions = itmes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // if it's not recycled, initialize some attributes
            LayoutInflater li = activity.getLayoutInflater();
            LinearLayout v = (LinearLayout) li.inflate(R.layout.gridview_item_layout, null);

            v.setPadding(15, 15, 15, 15);

            ITopeAction action = ((ITopeAction) getItem(position));
            TextView tv = (TextView) v.findViewById(R.id.gridActionText);
            tv.setText(action.getTitle());

            ImageView imageView = (ImageView) v.findViewById(R.id.gridActionImage);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(action.getItemId());
            /* need to store the image id as tag in order to be able to read it once it fires a click event */
            imageView.setTag(action.getItemId());
            topeActionViewMap.put(actions.get(position), v);

            imageView.setOnTouchListener(new ActionTouchListener());

            /* ON_LONG_CLICK */

            v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Context Menu");
                    menu.add(0, v.getId(), 0, "Add parameters");
                }
            });

            /* ON_CLICK */

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ITopeAction action = ((ITopeAction) TopeUtils.getAction(actions, v));
                    ActionCareTaker act = new ActionCareTaker(action, getActivity());
                    act.execute();

                    if (null != action && action.hasOppositeAction()) {

                        ImageView actionImage = (ImageView) v.findViewById(R.id.gridActionImage);
                        actionImage.setImageResource(action.getOppositeAction().getItemId());
                        actionImage.setTag(action.getOppositeAction().getItemId());

                        actionImage.setOnTouchListener(new ActionTouchListener());

                        TextView descriptionText = (TextView) v.findViewById(R.id.gridActionText);
                        descriptionText.setText(action.getOppositeAction().getTitle());

                        int indexToReplace = actions.indexOf(action);

                        action = action.getOppositeAction();

                        actions.set(indexToReplace, action);
                    }

                }
            });

            return v;
        } else {
            return (LinearLayout) convertView;
        }
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int position) {
        return actions.elementAt(position);
    }

    @Override
    public long getItemId(int position) {
        ITopeAction a = actions.elementAt(position);
        return a.getItemId();
    }

    public HashMap<ITopeAction, View> getTopeActionViewMap() {
        return topeActionViewMap;
    }

    public void setTopeActionViewMap(HashMap<ITopeAction, View> topeActionViewMap) {
        this.topeActionViewMap = topeActionViewMap;
    }

    public Vector<ITopeAction> getActions() {
        return actions;
    }

    public void setActions(Vector<ITopeAction> actions) {
        this.actions = actions;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}

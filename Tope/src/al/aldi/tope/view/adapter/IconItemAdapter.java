package al.aldi.tope.view.adapter;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeAction;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconItemAdapter<E> extends BaseAdapter {

    public static final int	WIDTH_160	= 160;
    public static final int	HEIGHT_250	= 250;
    Vector<ITopeAction>	actions;
    private Activity	activity;

    public IconItemAdapter() {
        super();
    }

    public IconItemAdapter(Activity activity, Vector<ITopeAction> itmes) {
        this.activity = activity;
        this.actions = itmes;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // if it's not recycled, initialize some attributes
            LayoutInflater li = activity.getLayoutInflater();
            LinearLayout v = (LinearLayout) li.inflate(R.layout.gridview_item_layout, null);

            v.setLayoutParams(new GridView.LayoutParams(WIDTH_160, HEIGHT_250));
            v.setPadding(15, 15, 15, 15);

            TextView tv = (TextView) v.findViewById(R.id.gridActionText);
            tv.setText(((ITopeAction) getItem(position)).getTitle());

            ImageView imageView = (ImageView) v.findViewById(R.id.gridActionImage);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(((ITopeAction) getItem(position)).getItemId());
            return v;
        } else {
            return (LinearLayout) convertView;
        }
    }

    public static View getSingleView(Activity activity, ITopeAction action) {
        LayoutInflater li = activity.getLayoutInflater();
        LinearLayout v = (LinearLayout) li.inflate(R.layout.gridview_item_layout, null);

        v.setLayoutParams(new GridView.LayoutParams(160, 250));
        v.setPadding(15, 15, 15, 15);

        TextView tv = (TextView) v.findViewById(R.id.gridActionText);
        tv.setText(action.getTitle());

        ImageView imageView = (ImageView) v.findViewById(R.id.gridActionImage);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(action.getItemId());
        return v;
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

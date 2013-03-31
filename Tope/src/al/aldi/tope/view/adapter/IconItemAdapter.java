package al.aldi.tope.view.adapter;

import java.util.Vector;

import al.aldi.tope.R;
import al.aldi.tope.controller.ITopeAction;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconItemAdapter<E> extends BaseAdapter {

    Vector<ITopeAction>	actions;
    private Activity		activity;

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

    public View B_getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = ((ITopeAction) getItem(position)).getImageView();
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(15, 15, 15, 15);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(((ITopeAction) getItem(position)).getItemId());
        return imageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = activity.getLayoutInflater();
        LinearLayout v = (LinearLayout) li.inflate(R.layout.gridview_item_layout, null);

        v.setLayoutParams(new GridView.LayoutParams(150, 150));
        v.setPadding(15, 15, 15, 15);

        TextView tv = (TextView) v.findViewById(R.id.gridActionText);
        tv.setText("Profile " + position);

        ImageView imageView = (ImageView) v.findViewById(R.id.gridActionImage);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(((ITopeAction) getItem(position)).getItemId());

        return v;
    }

}

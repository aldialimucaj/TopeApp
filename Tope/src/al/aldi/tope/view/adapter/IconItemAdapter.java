package al.aldi.tope.view.adapter;

import java.util.Vector;

import al.aldi.tope.controller.ITopeAction;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class IconItemAdapter<E> extends BaseAdapter {

    Vector<ITopeAction>	actions;
    private Context		context;

    public IconItemAdapter(Context context, Vector<ITopeAction> itmes) {
        this.context = context;
        this.actions = itmes;
    }

    @Override
    public int getCount() {
        System.out.println("IconItemAdapter.getCount("+actions.size()+")");
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
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = ((ITopeAction) getItem(position)).getImageView();
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(((ITopeAction) getItem(position)).getItemId());
        return imageView;
    }

}

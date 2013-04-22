package al.aldi.tope.view.adapter;

import java.util.List;

import al.aldi.tope.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ParametersActivityAdapter extends BaseAdapter {

    private List<String>	actions;
    private Activity				activity;

    public ParametersActivityAdapter(List<String> actions, Activity activity) {
        super();
        this.actions = actions;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public String getItem(int position) {
        return (String) actions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // if it's not recycled, initialize some attributes
            LayoutInflater li = activity.getLayoutInflater();
            LinearLayout v = (LinearLayout) li.inflate(R.layout.activity_parameters_items, null);

            v.setPadding(5, 5, 5, 5);

            TextView tv = (TextView) v.findViewById(R.id.actionName);
            tv.setText(getItem(position));

            return v;
        } else {
            return (LinearLayout) convertView;
        }
    }

}

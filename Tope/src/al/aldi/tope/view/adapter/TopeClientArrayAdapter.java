package al.aldi.tope.view.adapter;

import java.util.List;

import al.aldi.tope.R;
import al.aldi.tope.model.TopeClient;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TopeClientArrayAdapter extends ArrayAdapter<TopeClient> {

    private final Context			context;
    private List<TopeClient>	values;

    public TopeClientArrayAdapter(Context context, int resource, int textViewResourceId, List<TopeClient> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_handle_left, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.client_name_text);
        TextView telNrView = (TextView) rowView.findViewById(R.id.tel_nr);
        textView.setText(values.get(position).getName());
        telNrView.setText(values.get(position).getIp());

        CheckBox active = (CheckBox) rowView.findViewById(R.id.client_active);
        active.setChecked(values.get(position).isActive());
        active.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                values.get(position).setActive(isChecked);

            }
        });

        return rowView;
    }

}

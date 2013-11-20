package al.aldi.tope.view.adapter;

import al.aldi.tope.R;
import al.aldi.tope.model.TopeClient;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ScanServersArrayAdapter extends ArrayAdapter<TopeClient> {

    private Context          context         = null;
    private List<TopeClient> listTempServers = null;
    private int              port            = 0;

    public ScanServersArrayAdapter(Context context, int resource, int textViewResourceId, List<TopeClient> listStr, int port) {
        super(context, resource, textViewResourceId, listStr);
        this.context = context;
        this.port = port;
        this.listTempServers = listStr;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.client_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.client_list_name_text);
        TextView telNrView = (TextView) rowView.findViewById(R.id.client_list_ip);
        textView.setText(listTempServers.get(position).getName());
        telNrView.setText(listTempServers.get(position).getIp()+":"+ listTempServers.get(position).getPort());
        return rowView;
    }

    public List<TopeClient> getValues() {
        return listTempServers;
    }

    public void setValues(List<TopeClient> values) {
        this.listTempServers = listTempServers;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

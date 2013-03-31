package al.aldi.tope.view;

import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.R;
import al.aldi.tope.TopeUtils;
import al.aldi.tope.controller.ITopeAction;
import al.aldi.tope.controller.ITopeExecutable;
import al.aldi.tope.controller.SettingsMgr;
import al.aldi.tope.controller.TopeAction;
import al.aldi.tope.view.adapter.IconItemAdapter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static al.aldi.tope.TopeCommands.*;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class OsSectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String	ARG_SECTION_NUMBER	= "section_number";

    GridView					gridView;
    Vector<ITopeAction>			itmes				= new Vector<ITopeAction>();

    public OsSectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.gridview_fragment_os, container, false);
        final Resources res = getResources();

        gridView = (GridView) rootView.findViewById(R.id.fragmentGridView);

        initCommands(rootView); /* init the commands to show in the screen */

        IconItemAdapter<ITopeAction> adapter = new IconItemAdapter<ITopeAction>(getActivity(), itmes);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ((ITopeAction) itmes.elementAt(position)).execute();
            }
        });
        return rootView;
    }

    private void initCommands(View rootView) {

        itmes.add(TopeUtils.addAction(getActivity(), OS_POWER_OFF, R.drawable.system_shutdown));

        itmes.add(TopeUtils.addAction(getActivity(), OS_HIBERNATE, R.drawable.system_log_out));
    }

}
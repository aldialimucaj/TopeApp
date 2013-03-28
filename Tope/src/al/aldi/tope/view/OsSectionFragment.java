package al.aldi.tope.view;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import al.aldi.tope.R;
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

    public OsSectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.gridview_fragment_os, container, false);
        final Resources res = getResources();
        String[] numbers = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

        gridView = (GridView) rootView.findViewById(R.id.fragmentGridView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, numbers);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(rootView.getContext().getApplicationContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public View BAKonCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tope_os, container, false);
        final Resources res = getResources();
        ListView listView = (ListView) rootView.findViewById(R.id.mylist);
        final String[] values = new String[] { res.getString(R.string.os_op_standby) };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < values.length) {
                    if (values[position].equals(res.getString(R.string.os_op_standby))) {
                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    System.out.println("STAND BY");
                                    String url = "http://192.168.178.35:8080/os/lock_screen";
                                    HttpParams httpParameters = new BasicHttpParams();
                                    // Set the timeout in milliseconds until a connection is established.
                                    // The default value is zero, that means the timeout is not used.
                                    int timeoutConnection = 3000;
                                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                                    // Set the default socket timeout (SO_TIMEOUT)
                                    // in milliseconds which is the timeout for waiting for data.
                                    int timeoutSocket = 5000;
                                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                                    HttpClient client = new DefaultHttpClient();

                                    HttpGet get = new HttpGet(url);
                                    HttpContext localContext = new BasicHttpContext();
                                    HttpResponse res = client.execute(get, localContext);
                                    System.out.println(res.getStatusLine());

                                } catch (Exception e) {
                                    Log.e("[GET REQUEST]", "Network exception", e);
                                }
                            }
                        });

                        t.start();

                    }

                }
            }
        });

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        return rootView;
    }
}
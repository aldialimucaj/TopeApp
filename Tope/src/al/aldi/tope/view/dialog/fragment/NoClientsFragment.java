package al.aldi.tope.view.dialog.fragment;

import al.aldi.tope.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: Aldi
 * Date: 08.10.13
 * Time: 17:20
 * Fragment will be shown if there is no client registered or clients are not active.
 */
public class NoClientsFragment extends Fragment {
    public static String TAG = "al.aldi.tope.view.dialog.fragment.NoClientsFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView()");
//        ImageView elephatView = (ImageView) getActivity().findViewById(R.id.elephantNoClients);
//        elephatView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(NoClientsFragment.this.getActivity(), ClientAddEditActivity.class);
//                NoClientsFragment.this.startActivity(myIntent);
//                Log.i(TAG,"elephant was tapped!");
//            }
//        });


        return inflater.inflate(R.layout.fragment_no_clients,container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }
}

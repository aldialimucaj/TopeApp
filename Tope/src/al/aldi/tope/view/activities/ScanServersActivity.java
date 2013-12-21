package al.aldi.tope.view.activities;

import al.aldi.andorid.net.NetworkUtils;
import al.aldi.libjaldi.net.NetUtils;
import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.R;
import al.aldi.tope.controller.executables.ActionSynchExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.ActionSynchResponse;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.adapter.ScanServersArrayAdapter;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static al.aldi.tope.utils.TopeCommands.OS_SYNCH_ACTIONS;

/**
 * User: Aldi Alimucaj
 * Date: 17.11.13
 * Time: 23:28
 * <p/>
 * Activity shows a list of available servers in your local network. Offers the possibility to chose and add from the list
 * which saves time in comparison to manual adding.
 */
public class ScanServersActivity extends ListActivity {
    public static final String TAG = "al.aldi.tope.view.activities.ScanServersActivity";

    ProgressDialog progressDialog = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_scan_server, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchOnlineServersHelper();
    }

    /**
     * Adds a Progress Dialog to the scanning process.
     * See fetchOnlineServers() for more info.
     */
    private void fetchOnlineServersHelper() {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                String prefIp = PreferenceManager.getDefaultSharedPreferences(ScanServersActivity.this).getString("scan_ip", NetworkUtils.getWiFiIpAddress(getApplicationContext()));
                if (AldiStringUtils.isNullOrEmpty(prefIp))
                    prefIp = NetworkUtils.getWiFiIpAddress(getApplicationContext());
                String ipWithStar = prefIp.substring(0, prefIp.lastIndexOf(".") + 1) + "*";
                progressDialog = new ProgressDialog(ScanServersActivity.this);
                progressDialog.setTitle(getString(R.string.action_scan_progress_dialog_title));
                progressDialog.setMessage(AldiStringUtils.addRoundBrackets(ipWithStar) + " " + getString(R.string.action_scan_progress_dialog_message));
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    fetchOnlineServers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        asyncTask.execute((Void[]) null);
    }

    /**
     * Starts a scan network job which look up for online servers and adds them to the activity list.
     * You can call the helper if you want to see a progress dialog.
     */
    private void fetchOnlineServers() {
        String prefIp = PreferenceManager.getDefaultSharedPreferences(ScanServersActivity.this).getString("scan_ip", NetworkUtils.getWiFiIpAddress(getApplicationContext()));
        int prefPort = Integer.valueOf(TopeUtils.TOPE_DEFAULT_PORT);
        try {
            String prefPortStr = PreferenceManager.getDefaultSharedPreferences(ScanServersActivity.this).getString("scan_port", TopeUtils.TOPE_DEFAULT_PORT);
            prefPort = Integer.valueOf(prefPortStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> onlineServers = null;

        try {

            onlineServers = NetUtils.getListeningServers(prefIp, NetUtils.IpRange.MASK_24, prefPort);
            List<TopeClient> listClients = new ArrayList<TopeClient>();
            int index = 1;
            //TODO: before adding, check if listening server is truly a tope server!
            for (String str : onlineServers.keySet()) {
                listClients.add(new TopeClient("PC@" + index++, str, String.valueOf(prefPort), true));
            }

            final ScanServersArrayAdapter adapter = new ScanServersArrayAdapter(
                    getApplicationContext(), R.id.client_list_image_view, R.id.client_list_name_text, listClients, prefPort);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView list = getListView();
                    list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    list.setAdapter(adapter);
                }
            });


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        // registering list items for onClick event
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView list = getListView();
                registerForContextMenu(list);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ScanServersActivity.this, ClientAddEditActivity.class);

                        ListView list = ScanServersActivity.this.getListView();
                        Parcelable client = (TopeClient) list.getItemAtPosition(position);
                        i.putExtra(ClientsListActivity.INTENT_INSERT_INTO_DB, true);
                        i.putExtra(ClientsListActivity.INTENT_CLICKED_ITEM_ID, client);
                        startActivity(i);
                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scanned_clients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_add_all:
                ListView list = ScanServersActivity.this.getListView();
                for (int i = 0; i < list.getCount(); i++) {
                    TopeClient client = (TopeClient) list.getItemAtPosition(i);
                    addClient(client);
                }
                NavUtils.navigateUpFromSameTask(ScanServersActivity.this);
                break;
            case R.id.scan_add_selected:
                ListView listChecked = ScanServersActivity.this.getListView();
                for (int i = 0; i < listChecked.getCount(); i++) {
                    CheckBox cb = (CheckBox) listChecked.getChildAt(i).findViewById(R.id.client_list_active);
                    if (null != cb && cb.isChecked()) {
                        TopeClient client = (TopeClient) listChecked.getItemAtPosition(i);
                        addClient(client);
                    }
                }
                NavUtils.navigateUpFromSameTask(ScanServersActivity.this);
                break;
            case R.id.scan_rescan:
                fetchOnlineServersHelper();
                break;
            case R.id.scan_settings:
                startActivity(new Intent(this, ScanServersSettingsAcitivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * Adding Server/Server to the server list and synchronize its actions
     *
     * @param client
     */
    private void addClient(TopeClient client) {
        if (null == client) {
            Log.e(TAG, "Was expecting a client. Got null instead");
            return;
        }
        client.setActive(true);
        client.setContext(getApplicationContext());
        client.insertDb();
        final TopeClient tClient = client;

        // After adding the client into the DB, we synchronize its actions with the Server otherwise it wont
        // show any available actions.
        // Creating thread because this is the main thread
        new Thread(new Runnable() {

            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                        /* creating the synchronization action. no need to store this in the action list as this action is not shown in the grid */
                ITopeAction synchronizeAction = new TopeAction(OS_SYNCH_ACTIONS, 0, getString(R.string.client_edit_synchronize));
                synchronizeAction.setActionId(0);
                ActionSynchExecutor executor = new ActionSynchExecutor(synchronizeAction, getApplicationContext());
                synchronizeAction.setExecutable(executor);

                executor.setClient(tClient);

                Object response = synchronizeAction.execute(tClient);
                if (null == response || null == ((TopeResponse<ActionSynchResponse>) response).getPayload()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), getString(R.string.client_synch_could_not), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else if (null != response) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), ((TopeResponse<ActionSynchResponse>) response).getMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        }).start();
    }
}
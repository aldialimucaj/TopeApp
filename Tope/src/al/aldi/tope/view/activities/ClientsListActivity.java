package al.aldi.tope.view.activities;

import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.R;
import al.aldi.tope.controller.ActionCareTaker;
import al.aldi.tope.controller.executables.ActionSynchExecutor;
import al.aldi.tope.controller.executables.CallWithArgsExecutor;
import al.aldi.tope.model.*;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.model.responses.ActionSynchResponse;
import al.aldi.tope.view.adapter.TopeClientArrayAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import static al.aldi.tope.utils.TopeCommands.*;

/**
 * List Activity showing the clients.
 *
 * @author Aldi Alimucaj
 */
public class ClientsListActivity extends ListActivity {

    private static final String TAG                    = "al.aldi.tope.view.ClientsListActivity";
    public static final  String INTENT_CLICKED_ITEM_ID = "selected_id";
    public static final  String INTENT_INSERT_INTO_DB  = "inser_into_db";
    ClientDataSource source   = null;
    Intent           intent   = null;
    Uri              data     = null;
    ClipData         clipData = null;

    final int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    final int jellyBean         = Build.VERSION_CODES.JELLY_BEAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        intent = getIntent();
        if (null != intent && null != intent.getData()) {
            data = intent.getData();
        }

        if ((null != intent && currentApiVersion >= jellyBean) && null != intent.getClipData()) {
            clipData = intent.getClipData();
        }

        initListener();

        source = new ClientDataSource(getApplicationContext());
        source.open();
        List<TopeClient> clients = source.getAll();
        TopeClientArrayAdapter adapter = new TopeClientArrayAdapter(getApplicationContext(), R.id.client_list_image_view, R.id.client_list_name_text, clients);
        source.close();

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setAdapter(adapter);
        if (null != adapter && adapter.isEmpty()) {
            AskToCreateNewClient dialog = new AskToCreateNewClient();
            dialog.show(getFragmentManager(), "AskTag");
        }
        // Show the Up button in the action bar.
        setupActionBar();
    }

    private void initListener() {
        ListView list = this.getListView();
        registerForContextMenu(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ClientsListActivity.this, ClientAddEditActivity.class);

                ListView list = ClientsListActivity.this.getListView();
                Parcelable client = (TopeClient) list.getItemAtPosition(position);

                i.putExtra(INTENT_CLICKED_ITEM_ID, client);
                startActivity(i);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.client_add_edit_menu, menu);
    }

    private void executeOnClients() {
        String uri = null;
        if (null != data) {
            uri = data.toString();
        }
        if (null != clipData) {
            uri = clipData.getItemAt(0).getText().toString();
        }
        if (null != uri) {
            Log.i(TAG, "Uri: " + uri);

            if (AldiStringUtils.startsWithHttpS(uri)) {
                // TODO: this should be done by calling the database
                ITopeAction executeToClientAction = new TopeAction("openBrowserWithUrl", 0, getString(R.string.prog_op_openBrowserWithUrl));
                executeToClientAction.setCommandFullPath(PROG_BROWSER_OPEN_URL);
                executeToClientAction.setActionId(0);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(executeToClientAction, getApplicationContext());
                executeToClientAction.setExecutable(executor);

                ITopePayload payload = executeToClientAction.getPayload();

                try {
                    payload.addPayload(TopePayload.PARAM_ARG_0, uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = new ActionCareTaker(executeToClientAction, this);
                act.execute();
            } else {
                ITopeAction executeToClientAction = new TopeAction("readOutLoud", 0, getString(R.string.util_op_textToSpeech));
                executeToClientAction.setCommandFullPath(UTIL_READ_OUT_LOUD);
                executeToClientAction.setActionId(0);
                CallWithArgsExecutor executor = new CallWithArgsExecutor(executeToClientAction, getApplicationContext());
                executeToClientAction.setExecutable(executor);

                ITopePayload payload = executeToClientAction.getPayload();

                try {
                    payload.addPayload(TopePayload.PARAM_ARG_0, uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ActionCareTaker act = null;
                try {
                    act = new ActionCareTaker(executeToClientAction, this);
                    act.execute();
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        } else {
            // TODO: pass the intent to the child and back. dont lose it.
            Toast.makeText(getApplicationContext(), "Info was lost. Try again.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Log.i(TAG, "ClientsListActivity.onContextItemSelected(): [position]: " + position);
        ListView list = ClientsListActivity.this.getListView();
        switch (item.getItemId()) {
            case R.id.client_edit:
                Intent i = new Intent(ClientsListActivity.this, ClientAddEditActivity.class);

                Parcelable client = (TopeClient) list.getItemAtPosition(position);

                i.putExtra(INTENT_CLICKED_ITEM_ID, client);
                startActivity(i);
                return true;
            case R.id.client_synchronize:
                Toast.makeText(getApplicationContext(), getString(R.string.client_edit_synchronize_client), Toast.LENGTH_SHORT).show();

                //TODO: This thread is the same as ClientAddEditActivity.java:108. outsource
                /* Creating thread because this is the main thread */
                SynchTopeServers sts = new SynchTopeServers(info.position);
                sts.start();

                return true;
            case R.id.client_delete:
                AskDeleteClient adc = new AskDeleteClient(position);
                adc.show(getFragmentManager(), "AskDeleteSelected");

            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        source.close();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(ClientsListActivity.this);
        return;
    }

    // TODO: find smth else to delete many items at once.
    private void removeSelected() {
        ListView list = this.getListView();
        TopeClientArrayAdapter adapter = (TopeClientArrayAdapter) list.getAdapter();

        for (int i = 0; i < adapter.getValues().size(); i++) {
            TopeClient t = (TopeClient) adapter.getItem(i);
            if (t.isActive()) {
                adapter.getValues().remove(t);
                t.safeDelete();
                list.invalidateViews();

                return;
            }
        }
    }

    public void deleteSelected() {
        ListView list = this.getListView();
        TopeClientArrayAdapter adapter = (TopeClientArrayAdapter) list.getAdapter();
        int count = adapter.getValues().size(); // starting with the initial list
        for (int i = 0; i < count; i++) {
            removeSelected();
        }

        getListView().setAdapter(adapter);
    }

    public void deleteSelected(int position) {
        ListView list = ClientsListActivity.this.getListView();
        TopeClientArrayAdapter adapter = (TopeClientArrayAdapter) list.getAdapter();
        TopeClient t = (TopeClient) adapter.getItem(position);
        adapter.getValues().remove(t);
        t.safeDelete();
        list.invalidateViews();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    void init() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (null != data || null != clipData) {
            ListView list = this.getListView();
            registerForContextMenu(list);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String executeOnServer = getResources().getString(R.string.server_execute_on_pc);
                    Toast.makeText(getApplicationContext(), executeOnServer, Toast.LENGTH_SHORT).show();
                }
            });
            getMenuInflater().inflate(R.menu.clients_with_intent, menu);
        } else {
            getMenuInflater().inflate(R.menu.clients, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_client:
                startActivity(new Intent(this, ClientAddEditActivity.class));
                break;
            case R.id.action_delete_selected:
                AskDeleteClient adc = new AskDeleteClient();
                adc.show(getFragmentManager(), "AskDeleteSelected");
                break;
            case R.id.action_execute_on_clients2:
            case R.id.action_execute_on_clients:
                executeOnClients();
                break;
            case R.id.action_scan_network:
                Intent intent = new Intent(this, ScanServersActivity.class);
                //intent.putStringArrayListExtra("listStr", (ArrayList<String>) listStr);
                startActivity(intent);
                break;
            case R.id.clean_all_actions:
                ActionDataSource ads = new ActionDataSource(getApplicationContext());
                ads.open();
                ads.deleteAll();
                ads.close();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        System.out.println("ClientsListActivity.onSaveInstanceState()");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("ClientsListActivity.onRestoreInstanceState()");
    }

    class SynchTopeServers extends Thread {
        int itemPosition = -1;

        public SynchTopeServers(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        @Override
        public void run() {
            /* creating the synchronization action. no need to store this in the action list as this action is not shown in the grid */
            ITopeAction synchronizeAction = new TopeAction(OS_SYNCH_ACTIONS, 0, getString(R.string.client_edit_synchronize));
            synchronizeAction.setActionId(0);
            ActionSynchExecutor executor = new ActionSynchExecutor(synchronizeAction, getApplicationContext());
            synchronizeAction.setExecutable(executor);

            TopeClient client;
            ListView list = ClientsListActivity.this.getListView();
            client = (TopeClient) list.getItemAtPosition(itemPosition);
            executor.setClient(client);


            Object response = synchronizeAction.execute(client);
            if (null == response || null == ((TopeResponse<ActionSynchResponse>) response).getPayload()) {
                Looper.prepare();
                String strCouldNotSynch = getResources().getString(R.string.synch_failed);
                Toast.makeText(getApplicationContext(), strCouldNotSynch, Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (null != response) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), ((TopeResponse<ActionSynchResponse>) response).getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }

        int getItemPosition() {
            return itemPosition;
        }

        void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }
    }

    public class AskToCreateNewClient extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.client_list_empty);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getActivity(), ClientAddEditActivity.class));
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), R.string.client_list_empty_negative, Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }
    }

    public class AskDeleteClient extends DialogFragment {
        private int position = -1;

        public AskDeleteClient() {
        }

        public AskDeleteClient(int position) {
            this.position = position;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.client_list_delete);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (-1 != position) {
                        deleteSelected(position);
                    } else {
                        deleteSelected();
                    }

                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), R.string.client_list_delete_negative, Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }
    }
}

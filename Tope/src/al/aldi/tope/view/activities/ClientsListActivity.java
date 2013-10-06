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
import android.app.ListActivity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
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

import static al.aldi.tope.utils.TopeCommands.*;

/**
 * List Activity showing the clients.
 * 
 * @author Aldi Alimucaj
 * 
 */
public class ClientsListActivity extends ListActivity {

    private static final String TAG                    = "al.aldi.tope.view.ClientsListActivity";
    public static final String  INTENT_CLICKED_ITEM_ID = "selected_id";
    ClientDataSource            source                 = null;
    Intent                      intent                 = null;
    Uri                         data                   = null;
    ClipData                    clipData               = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        intent = getIntent();
        if (null != intent || null != intent.getData()) {
            data = intent.getData();
        }

        if (null != intent || null != intent.getClipData()) {
            clipData = intent.getClipData();
        }

        initListener();

        source = new ClientDataSource(getApplicationContext());
        source.open();
        TopeClientArrayAdapter adapter = new TopeClientArrayAdapter(getApplicationContext(), R.id.client_list_image_view, R.id.client_list_name_text, source.getAll());
        source.close();

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setAdapter(adapter);
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

                ActionCareTaker act = new ActionCareTaker(executeToClientAction, this);
                act.execute();
            }
        } else {
            // TODO: pass the intent to the child and back. dont lose it.
            Toast.makeText(getApplicationContext(), "Info was lost. Try again.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        System.out.println("ClientsListActivity.onContextItemSelected()");
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
        case R.id.client_edit:
            Toast.makeText(getApplicationContext(), "Edit Client", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.client_synchronize:
            Toast.makeText(getApplicationContext(), "Synchronize Client", Toast.LENGTH_SHORT).show();

            //TODO: This thread is the same as ClientAddEditActivity.java:108. outsource
            /* Creating thread because this is the main thread */
            new Thread(new Runnable() {

                @SuppressWarnings("unchecked")
                @Override
                public void run() {
                    /* creating the synchronization action. no need to store this in the action list as this action is not shown in the grid */
                    ITopeAction synchronizeAction = new TopeAction(OS_SYNCH_ACTIONS, 0, getString(R.string.client_edit_synchronize));
                    synchronizeAction.setActionId(0);
                    ActionSynchExecutor executor = new ActionSynchExecutor(synchronizeAction, getApplicationContext());
                    synchronizeAction.setExecutable(executor);

                    TopeClient client;
                    ListView list = ClientsListActivity.this.getListView();
                    client = (TopeClient) list.getItemAtPosition(info.position);
                    executor.setClient(client);


                    Object response = synchronizeAction.execute(client);
                    if (null == response || null == ((TopeResponse<ActionSynchResponse>) response).getPayload()) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Could not synchronize client. Please check your connection.", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else if (null != response) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), ((TopeResponse<ActionSynchResponse>) response).getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                }
            }).start();

            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        source.close();
    }

    // TODO: find smth else to delete many items at once.
    private void removeSelected() {
        ListView list = this.getListView();
        TopeClientArrayAdapter adapter = (TopeClientArrayAdapter) list.getAdapter();

        for (int i = 0; i < adapter.getValues().size(); i++) {
            TopeClient t = (TopeClient) adapter.getItem(i);
            if (t.isActive()) {
                adapter.getValues().remove(t);
                t.delete();
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
                    Toast.makeText(getApplicationContext(), "Click Execute instead on the Menu or Action Bar.", Toast.LENGTH_SHORT).show();
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
            deleteSelected();
            break;
        case R.id.action_execute_on_clients2:
        case R.id.action_execute_on_clients:
            executeOnClients();
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
}

package al.aldi.tope.view.activities;

import al.aldi.tope.R;
import al.aldi.tope.controller.executables.ActionSynchExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.ActionSynchResponse;
import al.aldi.tope.utils.TopeUtils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static al.aldi.tope.utils.TopeCommands.OS_SYNCH_ACTIONS;

/**
 * Activity for servers list view.
 */
public class ClientAddEditActivity extends Activity {

    protected TopeClient client       = null;
    private   boolean    insertIntoDb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add_edit);

        initData(); // in case of Edit where the client is handed over

        initListeners();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        client = (TopeClient) extras.getParcelable(ClientsListActivity.INTENT_CLICKED_ITEM_ID);
        insertIntoDb = extras.getBoolean(ClientsListActivity.INTENT_INSERT_INTO_DB);
        setDataFromClient(client);
    }

    protected void setDataFromClient(TopeClient client) {
        TextView cname = (TextView) findViewById(R.id.clientName);
        TextView cip = (TextView) findViewById(R.id.clientIp);
        TextView cport = (TextView) findViewById(R.id.clientPort);
        TextView cmac = (TextView) findViewById(R.id.clientMAC);
        Switch cactive = (Switch) findViewById(R.id.clientActive);
        TextView cuser = (TextView) findViewById(R.id.clientUser);
        TextView cpass = (TextView) findViewById(R.id.clientPass);
        TextView cdomain = (TextView) findViewById(R.id.clientDomain);

        cname.setText(client.getName());
        cip.setText(client.getIp());
        cport.setText(client.getPort());
        cmac.setText(client.getMac());
        cactive.setChecked(client.isActive());
        cuser.setText(client.getUser());
        cpass.setText(client.getPass());
        cdomain.setText(client.getDomain());
    }

    /**
     * Adds listeners to the buttons ok and cancel
     */
    private void initListeners() {
        Button okButton = (Button) findViewById(R.id.buttonOk);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView cname = (TextView) findViewById(R.id.clientName);
                TextView cip = (TextView) findViewById(R.id.clientIp);
                TextView cport = (TextView) findViewById(R.id.clientPort);
                TextView cmac = (TextView) findViewById(R.id.clientMAC);
                Switch cactive = (Switch) findViewById(R.id.clientActive);
                TextView cuser = (TextView) findViewById(R.id.clientUser);
                TextView cpass = (TextView) findViewById(R.id.clientPass);
                TextView cdomain = (TextView) findViewById(R.id.clientDomain);

                String name = cname.getText().toString();
                String ip = cip.getText().toString();
                String port = cport.getText().toString();
                String mac= cmac.getText().toString();
                String user = cuser.getText().toString();
                String pass = cpass.getText().toString();
                String domain = cdomain.getText().toString();
                boolean active = cactive.isChecked();

                // user forgets to set ip -> return
                if (ip.equals("")) {
                    Toast.makeText(ClientAddEditActivity.this, getString(R.string.client_create_ip_missing), Toast.LENGTH_SHORT).show();
                    return;
                }
                // user forgets to set port -> set to default port
                if (port.equals("")) {
                    Toast.makeText(ClientAddEditActivity.this, getString(R.string.client_create_port_missing) + " " + TopeUtils.TOPE_DEFAULT_PORT, Toast.LENGTH_SHORT).show();
                    port = TopeUtils.TOPE_DEFAULT_PORT;
                }
                // Adding the client if it didn't come form an intent -> ergo client == null
                // Or it came from an intent but it wasn't int the database it the flag force insert is true
                if (null == client || insertIntoDb) {
                    client = new TopeClient(name, ip, port, user, pass, domain, active);
                    client.setMac(mac);
                    client.setContext(getApplicationContext());
                    client.insertDb();
                } else {
                    TopeClient updateClinet = new TopeClient(name, ip, port, user, pass, domain, active);
                    updateClinet.setMac(mac);
                    updateClinet.setId(client.getId());
                    updateClinet.setContext(getApplicationContext());
                    updateClinet.updateDb();
                    client = updateClinet;
                }
                
                // Creating thread because this is the main thread
                // Starting synchronization for the changed client
                new Thread(new Runnable() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void run() {
                        /* creating the synchronization action. no need to store this in the action list as this action is not shown in the grid */
                        ITopeAction synchronizeAction = new TopeAction(OS_SYNCH_ACTIONS, 0, getString(R.string.client_edit_synchronize));
                        synchronizeAction.setActionId(0);
                        ActionSynchExecutor executor = new ActionSynchExecutor(synchronizeAction, getApplicationContext());
                        synchronizeAction.setExecutable(executor);

                        executor.setClient(client);

                        Object response = synchronizeAction.execute(client);
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

                NavUtils.navigateUpFromSameTask(ClientAddEditActivity.this);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ClientAddEditActivity.this);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_add_edit, menu);
        return true;
    }

}

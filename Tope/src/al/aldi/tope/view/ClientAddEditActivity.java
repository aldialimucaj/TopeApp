package al.aldi.tope.view;

import al.aldi.tope.R;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.utils.TopeUtils;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ClientAddEditActivity extends Activity {

    TopeClient client = null;

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
        setDataFromClient(client);
    }

    protected void setDataFromClient(TopeClient client) {
        TextView cname = (TextView) findViewById(R.id.clientName);
        TextView cip = (TextView) findViewById(R.id.clientIp);
        TextView cport = (TextView) findViewById(R.id.clientPort);
        Switch cactive = (Switch) findViewById(R.id.clientActive);
        TextView cuser = (TextView) findViewById(R.id.clientUser);
        TextView cpass = (TextView) findViewById(R.id.clientPass);
        TextView cdomain = (TextView) findViewById(R.id.clientDomain);

        cname.setText(client.getName());
        cip.setText(client.getIp());
        cport.setText(client.getPort());
        cactive.setChecked(client.isActive());
        cuser.setText(client.getUser());
        cpass.setText(client.getPass());
        cdomain.setText(client.getDomain());
    }

    private void initListeners() {
        Button okButton = (Button) findViewById(R.id.buttonOk);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView cname = (TextView) findViewById(R.id.clientName);
                TextView cip = (TextView) findViewById(R.id.clientIp);
                TextView cport = (TextView) findViewById(R.id.clientPort);
                Switch cactive = (Switch) findViewById(R.id.clientActive);
                TextView cuser = (TextView) findViewById(R.id.clientUser);
                TextView cpass = (TextView) findViewById(R.id.clientPass);
                TextView cdomain = (TextView) findViewById(R.id.clientDomain);

                String name = cname.getText().toString();
                String ip = cip.getText().toString();
                String port = cport.getText().toString();
                String user = cuser.getText().toString();
                String pass = cpass.getText().toString();
                String domain = cdomain.getText().toString();
                boolean active = cactive.isChecked();

                if (name.equals("")) {
                    Toast.makeText(ClientAddEditActivity.this, "Cannot crate host with no IP!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ip.equals("")) {
                    Toast.makeText(ClientAddEditActivity.this, "No Port set! Taking default port " + TopeUtils.TOPE_DEFAULT_PORT, Toast.LENGTH_SHORT).show();
                    port = TopeUtils.TOPE_DEFAULT_PORT;
                }
                if (null == client) {
                    TopeClient clinet = new TopeClient(name, ip, port, user, pass, domain, active);
                    clinet.setContext(getApplicationContext());
                    clinet.insertDb();
                } else {
                    TopeClient updateClinet = new TopeClient(name, ip, port, user, pass, domain, active);
                    updateClinet.setId(client.getId());
                    updateClinet.setContext(getApplicationContext());
                    updateClinet.updateDb();
                }

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

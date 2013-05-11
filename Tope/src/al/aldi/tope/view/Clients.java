package al.aldi.tope.view;

import al.aldi.tope.R;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.view.adapter.TopeClientArrayAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * List Activity showing the clients.
 *
 * @author Aldi Alimucaj
 *
 */
public class Clients extends ListActivity {
    ClientDataSource			source					= null;
    public static final String	INTENT_CLICKED_ITEM_ID	= "selected_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initListener();

        source = new ClientDataSource(getApplicationContext());
        source.open();

        TopeClientArrayAdapter adapter = new TopeClientArrayAdapter(getApplicationContext(), R.id.drag_handle, R.id.client_name_text, source.getAll());
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setAdapter(adapter);
        // Show the Up button in the action bar.
        setupActionBar();
    }

    private void initListener() {
        ListView list = this.getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Clients.this, ClientAddEditActivity.class);

                ListView list = Clients.this.getListView();
                Parcelable client = (TopeClient) list.getItemAtPosition(position);

                i.putExtra(INTENT_CLICKED_ITEM_ID, client);
                startActivity(i);
            }
        });
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
        getMenuInflater().inflate(R.menu.clients, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

}

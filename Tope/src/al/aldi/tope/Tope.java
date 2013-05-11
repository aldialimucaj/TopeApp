package al.aldi.tope;

import java.util.Iterator;
import java.util.List;

import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.view.Clients;
import al.aldi.tope.view.adapter.TopeSectionsPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main class and starting point
 *
 * @author Aldi Alimucaj
 *
 */
public class Tope extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    TopeSectionsPagerAdapter	mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager					mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tope);

        //test();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new TopeSectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    /**
     * Test Function that add some default clients
     */
    protected void test() {//TODO: Remove this function before production
        ClientDataSource source = new ClientDataSource(getApplicationContext());
        source.open();

        source.create("A-PC", "192.168.178.35", "8080");
        source.create("Greta-PC", "192.168.178.27", "8080");
        source.create("Puna-PC", "192.168.178.27", "8080");

        List<TopeClient> clients = source.getAll();

        for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext();) {
            TopeClient topeClient = (TopeClient) iterator.next();
            System.out.println(topeClient);
        }
        source.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tope, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_clients:
            startActivity(new Intent(this, Clients.class));
        }
        return super.onOptionsItemSelected(item);

    }

}

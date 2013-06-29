package al.aldi.tope;

import static al.aldi.tope.utils.TopeCommands.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import al.aldi.tope.controller.executables.PingExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.model.db.BaseProvider;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.view.activities.ClientsListActivity;
import al.aldi.tope.view.adapter.TopeSectionsPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
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
    TopeSectionsPagerAdapter mSectionsPagerAdapter;

    private static final int THREAD_SLEEP_CHECK_CLIENT = 1000;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager                mViewPager;
    PagerTabStrip            pagerTabStrip             = null;
    ClientDataSource         source;
    ActionDataSource         actionSource;
    boolean                  successful                = true;
    boolean                  stop                      = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tope);

        initDatabase();
        // test();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new TopeSectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(0x669900);

        source = new ClientDataSource(getApplicationContext());
        actionSource = new ActionDataSource(getApplicationContext());
        startStatusChecking();

    }

    private void startStatusChecking() {
        // here we get the value from the properties
        boolean ping = false;
        
        if (ping) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    ITopeAction action = new TopeAction("ping", 0, getString(R.string.util_op_ping));
                    action.setCommandFullPath(UTIL_PING);
                    action.setActionId(0);
                    PingExecutor executor = new PingExecutor(action, pagerTabStrip);
                    action.setExecutable(executor);

                    source.open();
                    actionSource.open();
                    while (!stop) {
                        @SuppressWarnings("rawtypes")
                        List<TopeResponse> topeResponses = new ArrayList<TopeResponse>();
                        List<TopeClient> clients = source.getAllActive(action.getMethod()); /* reads all acitve clients from the database */
                        // List<TopeClient> clients = source.getAllActive();/* reads all acitve clients from the database */
                        successful = true;
                        for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext();) {
                            TopeClient topeClient = (TopeClient) iterator.next();
                            @SuppressWarnings("rawtypes")
                            TopeResponse topeResponse = (TopeResponse) action.execute(topeClient);
                            successful &= topeResponse.isSuccessful();
                            topeResponses.add(topeResponse);
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (successful) {
                                    pagerTabStrip.setTabIndicatorColor(0x669900);
                                } else {
                                    pagerTabStrip.setTabIndicatorColor(0xFF3333);
                                }

                            }
                        });
                        // clearing the payload, as it is to be reset every time.
                        action.getPayload().clear();
                        try {
                            Thread.sleep(THREAD_SLEEP_CHECK_CLIENT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    source.close();
                    actionSource.close();

                }
            }).start();
        }
    }

    private void initDatabase() {
        BaseProvider source = new BaseProvider(getApplicationContext());
        source.getWritableDatabase();
        source.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stop = false;
        startStatusChecking();
    }

    /**
     * Test Function that add some default clients
     */
    protected void test() {// TODO: Remove this function before production
        ClientDataSource source = new ClientDataSource(getApplicationContext());
        source.open();

        source.create("A-PC-Prod", "192.168.178.35", "8080");
        source.create("A-PC-Test", "192.168.178.35", "8181");
        source.create("WIN8-Test1", "192.168.178.87", "8080");
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
            startActivity(new Intent(this, ClientsListActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

}

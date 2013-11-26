package al.aldi.tope;

import al.aldi.andorid.utils.PreferencesUtil;
import al.aldi.tope.controller.executables.PingExecutor;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.db.ActionDataSource;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.activities.ClientsListActivity;
import al.aldi.tope.view.activities.TopeSettingsAcitivity;
import al.aldi.tope.view.adapter.IconItemAdapter;
import al.aldi.tope.view.adapter.TopeSectionsPagerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static al.aldi.tope.utils.TopeCommands.UTIL_PING;

/**
 * Main class and starting point
 *
 * @author Aldi Alimucaj
 */
public class Tope extends FragmentActivity {

    public static final String TAG = "al.aldi.tope.Tope";

    TopeSectionsPagerAdapter mSectionsPagerAdapter;

    private static final int THREAD_SLEEP_CHECK_CLIENT = 10000; // 10s

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    PagerTabStrip    pagerTabStrip    = null;
    ClientDataSource clientDataSource = null;
    ActionDataSource actionDataSource = null;

    boolean stop = false;

    int clientSize = 0;
    int indexPing  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tope);

        // adding preference layout to settings menu
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        cleanUp();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new TopeSectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(0x030303);

        clientDataSource = TopeUtils.getClientDataSource(getApplicationContext());
        actionDataSource = TopeUtils.getActionDataSource(getApplicationContext());
        startStatusChecking();

    }

    /**
     * Cleans up some cache
     */
    private void cleanUp() {
        PreferencesUtil.remove(this, IconItemAdapter.TAG);
        Log.i(TAG, "Cleaning Up IconItme Cache...");
    }

    private void startStatusChecking() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean ping = prefs.getBoolean("ping_clients_checkbox", false);

        if (ping) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    ITopeAction action = new TopeAction("ping", 0, getString(R.string.util_op_ping));
                    action.setCommandFullPath(UTIL_PING);
                    action.setActionId(0);
                    PingExecutor executor = new PingExecutor(action, pagerTabStrip);
                    action.setExecutable(executor);

                    while (PreferenceManager.getDefaultSharedPreferences(Tope.this).getBoolean("ping_clients_checkbox", false)) {
                        @SuppressWarnings("rawtypes")
                        List<TopeResponse> topeResponses = new ArrayList<TopeResponse>();
                        List<TopeClient> clients = clientDataSource.getAllActive();
                        clientSize = clients.size();
                        indexPing = 0;

                        for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext(); ) {
                            TopeClient topeClient = (TopeClient) iterator.next();
                            @SuppressWarnings("rawtypes")
                            TopeResponse topeResponse = (TopeResponse) action.execute(topeClient);
                            boolean successfulPing = topeResponse.isSuccessful();
                            if (successfulPing) {
                                indexPing++;
                            }
                            topeResponses.add(topeResponse);
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (indexPing < clientSize) {
                                    if (indexPing == 0) {
                                        // RED
                                        pagerTabStrip.setTabIndicatorColor(0xA40004);
                                    } else {
                                        // YELLOW
                                        pagerTabStrip.setTabIndicatorColor(0xFFF400);
                                    }
                                } else {
                                    // GREEN
                                    pagerTabStrip.setTabIndicatorColor(0x00CC00);
                                }

                            }
                        });
                        // clearing the payload, as it is to be reset every
                        // time.
                        action.getPayload().clear();
                        try {
                            Thread.sleep(THREAD_SLEEP_CHECK_CLIENT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            pagerTabStrip.setTabIndicatorColor(0x030303);
        }
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
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, TopeSettingsAcitivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}

package al.aldi.tope;

import java.util.Iterator;
import java.util.List;

import al.aldi.tope.controller.SettingsMgr;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.model.db.ClientDataSource;
import al.aldi.tope.view.TopeSectionsPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

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

        SettingsMgr sMgr = SettingsMgr.getInstance();
        sMgr.setServerName("192.168.178.35");
        // sMgr.setServerName("192.168.0.2");
        sMgr.setPort(8080);

        test();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new TopeSectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    private void test() {
        ClientDataSource source = new ClientDataSource(getApplicationContext());
        source.open();

        TopeClient client = source.create("A-PC", "192.168.178.35", "8080");
        System.out.println(client);

        List<TopeClient> clients = source.getAll();
        for (Iterator iterator = clients.iterator(); iterator.hasNext();) {
            TopeClient topeClient = (TopeClient) iterator.next();
            System.out.println(topeClient);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tope, menu);
        return true;
    }

}

package al.aldi.tope.view;

import java.util.Locale;

import al.aldi.tope.R;
import al.aldi.tope.Tope;
import al.aldi.tope.R.string;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TopeSectionsPagerAdapter extends FragmentPagerAdapter {

    /**
     *
     */
    private final Tope	tope;

    public TopeSectionsPagerAdapter(Tope tope, FragmentManager fm) {
        super(fm);
        this.tope = tope;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        Fragment fragment = new OsSectionFragment();
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
        case 0:
            return this.tope.getString(R.string.title_section1).toUpperCase(l);
        case 1:
            return this.tope.getString(R.string.title_section2).toUpperCase(l);
        case 2:
            return this.tope.getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }
}
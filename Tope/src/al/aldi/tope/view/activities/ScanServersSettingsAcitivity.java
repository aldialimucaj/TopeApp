package al.aldi.tope.view.activities;

import al.aldi.andorid.net.NetworkUtils;
import al.aldi.tope.R;
import al.aldi.tope.utils.TopeUtils;
import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

public class ScanServersSettingsAcitivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    private static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_scan_server);

            findPreference("scan_ip").setDefaultValue(NetworkUtils.getWiFiIpAddress(getActivity()));
            if (((EditTextPreference) findPreference("scan_port")).getText() == null) {
                ((EditTextPreference) findPreference("scan_port")).setText(TopeUtils.TOPE_DEFAULT_PORT);
            }

            if (((EditTextPreference) findPreference("scan_ip")).getText() == null) {
                ((EditTextPreference) findPreference("scan_ip")).setText(NetworkUtils.getWiFiIpAddress(getActivity()));
            }
        }
    }
}

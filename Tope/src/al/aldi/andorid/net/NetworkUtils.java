package al.aldi.andorid.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Helper class for general network functions.
 *
 * @author Aldi Alimucaj
 *
 */
public class NetworkUtils {

    /**
     * Checks if network is available.
     *
     * @param context
     * @return true if network connection.
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}

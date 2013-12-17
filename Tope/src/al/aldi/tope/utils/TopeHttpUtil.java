package al.aldi.tope.utils;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.model.JsonTopeResponse;
import al.aldi.tope.model.TopeResponse;
import al.aldi.tope.model.responses.EmptyResponse;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Helper class to translate tope requests into tope responses.
 *
 * @author Aldi Alimucaj
 */
public class TopeHttpUtil<E> {

    private static final String TAG = "al.aldi.tope.utils.TopeHttpUtil";
    Gson gson = new GsonBuilder().setDateFormat(JsonTopeResponse.DATE_FORMAT_FULL).create();

    E tr;
    private int socketTimeout     = 1000;
    private int connectionTimeout = 1000;

    public TopeHttpUtil(E response) {
        this();
        this.tr = response;
    }

    public TopeHttpUtil() {
        this.socketTimeout = HttpUtils.SOCKET_TIMEOUT;
        this.connectionTimeout = HttpUtils.CONNECTION_TIMEOUT;
    }


    public TopeHttpUtil(int socketTimeout, int connectionTimeout) {
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Sends a get request to the following url and returns true if Server
     * responds with successful request. CODE 200
     *
     * @param url    url to be called
     * @param params hashmap with params
     * @return true if code 200
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public E sendPostRequestWithParams(final String url, final HashMap<String, String> params) {
        HttpResponse res = new HttpUtils(socketTimeout, connectionTimeout).sendPostRequestWithParams(url, params);
        if (null == res) {
            tr = (E) new TopeResponse<EmptyResponse>();
            return tr;
        }

        try {
            String jsonString = new HttpUtils(socketTimeout, connectionTimeout).httpEntitiyToSafeString(res.getEntity());
            Type responseType = new TypeToken<E>() {
            }.getType();
            tr = gson.fromJson(jsonString, responseType);
            ((TopeResponse)tr).setStatus(res.getStatusLine().getStatusCode());
            return tr;// RETURN
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tr; // return default empty response in order not to return null
    }

    public String sendPostRequestWithParamsRetString(final String url, final HashMap<String, String> params) {
        HttpResponse res = new HttpUtils(socketTimeout, connectionTimeout).sendPostRequestWithParams(url, params);
        if (null == res) {
            return "{}";
        }

        try {
            String jsonString = new HttpUtils(socketTimeout, connectionTimeout).httpEntitiyToSafeString(res.getEntity());
            return jsonString;// RETURN
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{}"; // return default empty response in order not to return null
    }

    public void printResponseToJSON(HttpResponse res) {
        String json = new HttpUtils(socketTimeout, connectionTimeout).httpEntitiyToSafeString(res.getEntity());
        Gson gson = new GsonBuilder().setDateFormat(JsonTopeResponse.DATE_FORMAT_FULL).create();
        ;
        @SuppressWarnings("rawtypes")
        TopeResponse response = gson.fromJson(json, TopeResponse.class);
        Log.i(TAG, response.toString());
    }
}

package al.aldi.tope.utils;

import static al.aldi.tope.model.TopeResponse.JSON_RES_STATUS_CODE;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import al.aldi.andorid.net.HttpUtils;
import al.aldi.tope.model.TopeResponse;

/**
 * Helper class to translate tope requests into tope responses.
 *
 * @author Aldi Alimucaj
 *
 */
public class TopeHttpUtil {
    /**
     * Sends a get request to the following url and returns true if Server
     * responds with successful request. CODE 200
     *
     * @param url
     *            url to be called
     * @param params
     *            hashmap with params
     * @return ture if code 200
     */
    public static TopeResponse sendPostRequestWithParams(final String url, final HashMap<String, String> params) {
        HttpResponse res = HttpUtils.sendPostRequestWithParams(url, params);
        if(null == res){
            return new TopeResponse();
        }

        TopeResponse tr;
        try {
            /* Reading the response */
            JSONObject jo = HttpUtils.httpEntitiyToJson(res.getEntity());
            /* Add to the response the default status code which comes through the http response */
            jo.put(JSON_RES_STATUS_CODE, res.getStatusLine().getStatusCode());
            tr = new TopeResponse(jo);
            // TODO: remove
            System.out.println(jo);

            return tr;//RETURN

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new TopeResponse(); // return default empty response in order not to return null
    }
}

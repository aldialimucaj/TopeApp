package al.aldi.andorid.net;

import static al.aldi.tope.model.TopeResponse.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import al.aldi.tope.TopeUtils;
import al.aldi.tope.model.TopeResponse;

public class HttpUtils {

    public static final String	STATUS_CODE_SUCCESS	= "200";

    /**
     * Sends a get request to the following url and returns true if Server
     * responds with successful request. CODE 200
     *
     * @param url
     * @return ture if code 200
     */
    public static TopeResponse sendGetRequest(final String url) {
        Callable<TopeResponse> request = new Callable<TopeResponse>() {

            @Override
            public TopeResponse call() {

                HttpClient client = getDefaultClient();

                HttpGet get = new HttpGet(url);
                // get.setHeader("Content-Type", "application/json");
                get.addHeader("Accept", "application/json");

                HttpContext localContext = new BasicHttpContext();
                HttpResponse res = null;
                try {
                    res = client.execute(get, localContext);
                } catch (ClientProtocolException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }

                JSONObject jo = httpEntitiyToJson(res.getEntity());
                try {
                    jo.put(JSON_RES_STATUS_CODE, res.getStatusLine().getStatusCode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TopeResponse tr = new TopeResponse(jo);
                System.out.println("HttpUtils.sendGetRequest(...).new Callable() {...}.call()");
                System.out.println(jo);
                return tr;
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<TopeResponse> future = pool.submit(request);

        TopeResponse topeResponse = null;

        try {
            topeResponse = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return topeResponse;
    }

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
        Callable<TopeResponse> request = new Callable<TopeResponse>() {

            @Override
            public TopeResponse call() {
                HttpContext localContext = new BasicHttpContext();
                HttpResponse res = null;

                /* Standard parameters to limit the timeout */
                HttpClient client = getDefaultClient();

                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("Accept", "application/json"); /* in order to let the server know we accept json */

                /* reading the parameter list and adding it to the entity */
                List<NameValuePair> httpParams = new ArrayList<NameValuePair>();

                for (String key : params.keySet()) {
                    String value = params.get(key);
                    httpParams.add(new BasicNameValuePair(key, value));
                }

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(httpParams, "UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                /* finished adding params */

                /* Executing the call */
                try {
                    res = client.execute(httpPost, localContext);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /* Reading the response */
                JSONObject jo = httpEntitiyToJson(res.getEntity());
                /* Add to the response the default status code which comes through the http response */
                try {
                    jo.put(JSON_RES_STATUS_CODE, res.getStatusLine().getStatusCode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TopeResponse tr = new TopeResponse(jo);
                // TODO: remove
                System.out.println(jo);

                return tr;
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<TopeResponse> future = pool.submit(request);

        TopeResponse topeResponse = null;

        try {
            topeResponse = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return topeResponse;
    }

    /**
     * After sending asynchronously the get request this method waits for
     * the response which it then sends back to the caller.
     *
     * @param url
     *            URI to call
     * @return the response from the other peer
     */
    public static HttpResponse sendRequest(final String url) {
        Callable<HttpResponse> request = new Callable<HttpResponse>() {

            @Override
            public HttpResponse call() {
                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = 3000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 5000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpGet get = new HttpGet(url);
                HttpContext localContext = new BasicHttpContext();
                HttpResponse res = null;
                try {
                    res = client.execute(get, localContext);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(res.getStatusLine());

                return res;
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<HttpResponse> future = pool.submit(request);

        HttpResponse success = null;

        try {
            success = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static boolean sendPostRequest(String url) {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        HttpClient client = new DefaultHttpClient(httpParameters);

        HttpPost get = new HttpPost(url);
        HttpContext localContext = new BasicHttpContext();
        HttpResponse res = null;
        try {
            res = client.execute(get, localContext);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.getStatusLine().getStatusCode() == 200;
    }

    public static DefaultHttpClient getDefaultClient() {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        return new DefaultHttpClient(httpParameters);
    }

    // *****************************************************************************//

    @Deprecated
    public static boolean sendGetRequest_old(final String url) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = 3000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 5000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpGet get = new HttpGet(url);
                HttpContext localContext = new BasicHttpContext();
                try {
                    client.execute(get, localContext);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        return true;
    }

    public static String httpEntitiyToString(HttpEntity entity) {
        StringBuilder builder = new StringBuilder();
        InputStream content = null;
        try {
            content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                content.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

    public static JSONObject httpEntitiyToJson(HttpEntity entity) {
        JSONObject jObject = null;
        try {
            String jsonStr = httpEntitiyToString(entity);
            jsonStr = StringEscapeUtils.unescapeJava(jsonStr);
            int strSize = jsonStr.length();
            jsonStr = jsonStr.substring(1, strSize - 1);
            jObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObject;
    }

}

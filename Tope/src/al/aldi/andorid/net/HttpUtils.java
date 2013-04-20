package al.aldi.andorid.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import al.aldi.tope.TopeUtils;

public class HttpUtils {


    public static final String		STATUS_CODE_SUCCESS	= "200";

    /**
     * Sends a get request to the following url and returns true if Server
     * responds with successful request. CODE 200
     *
     * @param url
     * @return ture if code 200
     */
    public static JSONObject sendGetRequest(final String url) {
        Callable<JSONObject> request = new Callable<JSONObject>() {

            @Override
            public JSONObject call() {
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 3000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 5000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpGet get = new HttpGet(url);
                get.setHeader("Content-Type", "application/json");
                get.addHeader("Accept", "application/json");

                HttpContext localContext = new BasicHttpContext();
                HttpResponse res = null;
                try {
                    res = client.execute(get, localContext);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject jo = httpEntitiyToJson(res.getEntity());
                try {
                    jo.put(TopeUtils.JSON_RES_STATUS_CODE, res.getStatusLine().getStatusCode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(jo);
                return jo;
            }
        };

        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<JSONObject> future = pool.submit(request);

        JSONObject jsonResponse = null;

        try {
            jsonResponse = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return jsonResponse;// res.getStatusLine().getStatusCode() == 200;
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

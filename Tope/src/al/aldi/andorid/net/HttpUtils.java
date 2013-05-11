package al.aldi.andorid.net;


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
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class for sending and receiving http requests.
 *
 * @author Aldi Alimucaj
 *
 */
public class HttpUtils {
    public static final String	LOG_TAG				= "al.aldi.andorid.net.HttpUtils";
    public static final String	STATUS_CODE_SUCCESS	= "200";

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
    public static HttpResponse sendPostRequestWithParams(final String url, final HashMap<String, String> params) {
        HttpContext localContext = new BasicHttpContext();
        HttpResponse res = null;

        /* Standard parameters to limit the timeout */
        HttpClient client = getDefaultClient();
        client = ignoreSslClient(client);// TODO: Remove this or make it optional

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
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }

    /**
     * Returns a client which ignores ssl validation and verification.
     *
     * @param client
     * @return
     */
    private static HttpClient ignoreSslClient(HttpClient client) {
        try {
            SSLSocketFactory ssf = new MySSLSocketFactory(null, null);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 80)); // the default port doesnt seem to have a big effect
            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * After sending asynchronously the get request this method waits for
     * the response which it then sends back to the caller.
     *
     * @param url
     *            URI to call
     * @return the response from the other peer
     */
    public static HttpResponse sendGetRequest(final String url) {
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

    /**
     * Sends post request to url. Ignores the response.
     *
     * @param url
     * @return true if response code is 200
     */
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

    /**
     * returns a client with predefined parameters for timeout and socket timeout.
     *
     * @return
     */
    private static DefaultHttpClient getDefaultClient() {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        return new DefaultHttpClient(httpParameters);
    }

    /**
     * Reads the content out of the entity and returns it as a string.
     *
     * @param entity
     * @return http content
     */
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

    /**
     * Returns the entity's content as a JSONObject.
     *
     * @param entity
     * @return
     * @throws JSONException
     */
    public static JSONObject httpEntitiyToJson(HttpEntity entity) throws JSONException {
        JSONObject jObject = null;
        String jsonStr = httpEntitiyToString(entity);
        System.out.println(jsonStr);
        jsonStr = StringEscapeUtils.unescapeJava(jsonStr); /* need to excape from \n form */
        int strSize = jsonStr.length();
        jsonStr = jsonStr.substring(1, strSize - 1); /* removing leading quotes */
        jObject = new JSONObject(jsonStr);
        return jObject;
    }
}

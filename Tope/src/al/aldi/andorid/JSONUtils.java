package al.aldi.andorid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.JsonWriter;

public class JSONUtils {

    private Activity	activity		= null;
    private String		dbConnection	= null;
    private JSONObject	jo				= null;

    public JSONUtils(JSONObject jo) {
        this.jo = jo;
    }

    public JSONUtils(Activity activity) {
        this.activity = activity;
    }

    public JSONUtils(Activity activity, String dbConnection) {
        super();
        this.activity = activity;
        this.dbConnection = dbConnection;
    }

    public void writeFile(String fileName, String id, String value) {
        FileOutputStream fos;
        try {
            fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            writer.setIndent("  ");
            writeAttrToJsonWriter(writer, id, value);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void editFile(String fileName, String id, String value) {

        try {
            File file = new File(activity.getFilesDir(), fileName);
            JSONObject json = new JSONObject(readFile(fileName));
            json.put(id, value);

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            fw.write(json.toString());
            fw.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAllAttr(String fileName, HashMap<String, String> map) {
        for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
            String key = i.next();
            editFile(fileName, key, map.get(key));
        }
    }

    public void writeAttr(String fileName, String id, String value) {
        editFile(fileName, id, value);
    }

    private void writeAttrToJsonWriter(JsonWriter writer, String id, String value) throws IOException {
        writer.beginObject();
        writer.name(id).value(value);
        writer.endObject();
    }

    /**
     * Check if file exists. The filename is relative to the app directory.
     * It will add the absolute dirctory from the activity.
     *
     * {@code fileExists("testFile.json");}
     *
     * @param fileName
     * @return true if file is found
     */
    public boolean fileExists(String fileName) {
        File file = new File(activity.getFilesDir(), fileName);
        return file.exists();
    }

    /**
     * Reading Attributes
     *
     * @param fileName
     * @param id
     * @return
     * @throws JSONException
     */
    public String readAttr(String fileName, String id) throws JSONException {
        JSONObject object = getJSONObject(fileName);
        return readAttrFromJSON(object.toString(), id);
    }

    public String readAttr(JSONObject object, String id) throws JSONException {
        return readAttrFromJSON(object, id);
    }

    public String readAttr(String id) throws JSONException {
        return readAttrFromJSON(jo, id);
    }


    public HashMap<String, String> readAllAttr(String fileName) throws JSONException {
        JSONObject object = getJSONObject(fileName);
        return readAllAttr(object);
    }

    public HashMap<String, String> readAllAttr(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = object.getString(key);
            map.put(key, value);
        }
        return map;
    }

    private String readAttrFromJSON(String json, String id) throws JSONException {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!jObject.has(id)) {
            return "";
        }
        return jObject.getString(id);
    }

    private String readAttrFromJSON(JSONObject jObject, String id) throws JSONException {
        if (!jObject.has(id)) {
            return "";
        }
        return jObject.getString(id);
    }

    private String readFile(String path) throws IOException {
        File file = new File(activity.getFilesDir(), path);
        if (file.exists()) {
            FileInputStream stream = new FileInputStream(file);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                String retString = Charset.defaultCharset().decode(bb).toString();
                return retString;
            } finally {
                stream.close();
            }
        }
        return "{}";// Empty json
    }

    public JSONObject getJSONObject(String fileName) {
        JSONObject object = null;
        try {
            object = new JSONObject(readFile(fileName));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void serializeFile(String fileName, String string, String value) {
        File file = new File(activity.getFilesDir(), fileName);
        if (file.exists()) {
            editFile(fileName, string, value);
        } else {
            writeFile(fileName, string, value);
        }
    }

    public void serializeFile(String fileName, String string, String value, boolean override) {
        if (!override) {
            editFile(fileName, string, value);
        } else {
            writeFile(fileName, string, value);
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(String dbConnection) {
        this.dbConnection = dbConnection;
    }

}

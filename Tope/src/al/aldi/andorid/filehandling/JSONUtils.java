package al.aldi.andorid.filehandling;

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
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.JsonWriter;

/**
 * Helper class that supports reading, editing and saving of JSON objects.
 *
 * @author Aldi Alimucaj
 *
 */
public class JSONUtils {

    /**
     * The activity is needed to be able to persist the object into andorid's storage system.
     */
    private Activity	activity		= null;
    /**
     * the url to the internal andorid database
     */
    private String		dbConnection	= null;

    /**
     * the private JSON object which is being forged.
     */
    private JSONObject	jo				= null;

    /**
     * Start the object with a predefined json structure.
     *
     * @param jo
     */
    public JSONUtils(JSONObject jo) {
        this.jo = jo;
    }

    /**
     * Start with just the activity to work with.
     *
     * @param activity
     */
    public JSONUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * A new object most likely to be used in persisting a JSON File.
     *
     * @param activity
     * @param dbConnection
     */
    public JSONUtils(Activity activity, String dbConnection) {
        super();
        this.activity = activity;
        this.dbConnection = dbConnection;
    }

    /**
     * Edits file from/into the internal andorid storage.
     * This method uses JsonWriter as engine to write the file.
     *
     * @param fileName
     *            Filename starting from the apps repository.
     * @param id
     *            key to be added
     * @param value
     *            value of the key
     */
    private void writeFile(String fileName, String id, String value) {
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

    /**
     * Edits file from/into the internal andorid storage.
     * This method uses JSONObject as engine to write the file.
     *
     * @param fileName
     * @param id
     * @param value
     */
    private void writeEditFile(String fileName, String id, String value) {

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

    /**
     * Writes all attributes with key and value to the json file
     * depicted by the filename.
     *
     * @param fileName
     * @param map
     */
    public void writeAllAttr(String fileName, HashMap<String, String> map) {
        for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
            String key = i.next();
            writeEditFile(fileName, key, map.get(key));
        }
    }

    /**
     * Write a single attribute the file.
     *
     * @param fileName
     * @param id
     * @param value
     */
    public void writeAttr(String fileName, String id, String value) {
        writeEditFile(fileName, id, value);
    }

    /**
     * Used by {@link} writeFile()
     *
     * @param writer
     * @param id
     * @param value
     * @throws IOException
     */
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
     * @return String of attribute value
     * @throws JSONException
     */
    public String readAttr(String fileName, String id) throws JSONException {
        JSONObject object = getJSONObject(fileName);
        return readAttrFromJSON(object.toString(), id);
    }

    /**
     * Read attribute from JSONObject
     *
     * @param object
     * @param id
     * @return String
     * @throws JSONException
     */
    public String readAttr(JSONObject object, String id) throws JSONException {
        return readAttrFromJSON(object, id);
    }

    /**
     * Read attribute from member JSONObject
     *
     * @param id
     * @return
     * @throws JSONException
     */
    public String readAttr(String id) throws JSONException {
        return readAttrFromJSON(jo, id);
    }

    /**
     * Read all attributes from filename json object.
     *
     * @param fileName
     * @return HashMap<String, String>
     * @throws JSONException
     */
    public HashMap<String, String> readAllAttr(String fileName) throws JSONException {
        JSONObject object = getJSONObject(fileName);
        return readAllAttr(object);
    }

    /**
     * Read all attribute from specific JSONObject.
     *
     * @param object
     * @return HashMap<String, String>
     * @throws JSONException
     */
    public HashMap<String, String> readAllAttr(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = object.getString(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Read all attribute from member JSONObject.
     *
     * @param object
     * @return HashMap<String, String>
     * @throws JSONException
     */
    public HashMap<String, String> readAllAttr() throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Iterator<String> keys = jo.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = jo.getString(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Reading value form JSONObject File
     *
     * @param json
     *            Filename load
     * @param id
     * @return String
     * @throws JSONException
     */
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

    /**
     * Reading value form JSONObject File
     *
     * @param jObject
     *            object to read from
     * @param id
     * @return String
     * @throws JSONException
     */
    private String readAttrFromJSON(JSONObject jObject, String id) throws JSONException {
        if (!jObject.has(id)) {
            return "";
        }
        return jObject.getString(id);
    }

    /**
     * Reads a file and returns a String content.
     *
     * @param path
     * @return content
     * @throws IOException
     */
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

    /**
     * Returns JSONObject form filepath.
     *
     * @param fileName
     * @return
     */
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

    /**
     * Write to file.
     *
     * @param fileName
     * @param string
     * @param value
     */
    @Deprecated
    public void serializeFile(String fileName, String string, String value) {
        File file = new File(activity.getFilesDir(), fileName);
        if (file.exists()) {
            writeEditFile(fileName, string, value);
        } else {
            writeFile(fileName, string, value);
        }
    }

    /**
     * Write to file with override option.
     *
     * @param fileName
     * @param string
     * @param value
     * @param override
     */
    @Deprecated
    public void serializeFile(String fileName, String string, String value, boolean override) {
        if (!override) {
            writeEditFile(fileName, string, value);
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

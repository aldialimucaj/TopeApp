package al.aldi.tope.model.db;

import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.model.TopeClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Vector;

import static al.aldi.tope.model.db.ActionOpenHelper.ACTION_TABLE_NAME;
import static al.aldi.tope.model.db.ActionOpenHelper.METHOD;
import static al.aldi.tope.model.db.ClientOpenHelper.*;
import static android.util.Log.e;
import static android.util.Log.i;

/**
 * A wrapper class for accessing tope clients from android inbuilt database.
 *
 * @author Aldi Alimucaj
 */
public class ClientDataSource {
    public static final String TAG = "al.aldi.tope.model.db.ClientDataSource";
    // Database fields
    private SQLiteDatabase   database;
    private ClientOpenHelper dbClientHelper;
    private String[] allColumns = {CLIENT_OWN_ID, CLIENT_NAME, CLIENT_IP, CLIENT_PORT, CLIENT_USER, CLIENT_PASS, CLIENT_ACTIVE, CLIENT_DOMAIN, CLIENT_MAC};
    private Context context;

    static ClientDataSource instance;

    public ClientDataSource(Context context) {
        super();
        this.context = context;
        this.dbClientHelper = new ClientOpenHelper(context);
    }

    /**
     * Create database handler
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        try {
            database = dbClientHelper.getWritableDatabase();
            database.beginTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Check if DB is open and ready to accept queries.
     *
     * @return
     */
    public boolean isOpen() {
        return null != database && database.isOpen();
    }

    /**
     * Close Database Handler.
     */
    public void close() {
        if (isOpen()) {
            try {
                database.setTransactionSuccessful();
                database.endTransaction();
                dbClientHelper.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a new client into the database.
     *
     * @param client
     * @return
     */
    public boolean create(TopeClient client) {

        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, client.getName());
        values.put(CLIENT_IP, client.getIp());
        values.put(CLIENT_PORT, client.getPort());
        values.put(CLIENT_ACTIVE, client.isActive());
        values.put(CLIENT_USER, client.getUser());
        values.put(CLIENT_PASS, client.getPass());
        values.put(CLIENT_DOMAIN, client.getDomain());
        values.put(CLIENT_MAC, client.getMac());

        long insertId = 0;
        try {
            insertId = database.insert(CLIENT_TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.setId(insertId);

        return insertId != -1;

    }

    /**
     * Create a client from method parameters.
     *
     * @param name
     * @param ip
     * @param port
     * @return created client or null if unsuccessful
     */
    public TopeClient create(String name, String ip, String port) {
        TopeClient client = null;
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, name);
        values.put(CLIENT_IP, ip);
        values.put(CLIENT_PORT, port);

        long insertId = 0;
        try {
            insertId = database.insert(CLIENT_TABLE_NAME, null, values);
            Cursor cursor = database.query(CLIENT_TABLE_NAME, allColumns, ClientOpenHelper.CLIENT_OWN_ID + " = " + insertId, null, null, null, null);
            cursor.moveToFirst();
            client = cursorToClient(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * Get all clients.
     *
     * @return
     */
    public Vector<TopeClient> getAll() {
        Vector<TopeClient> vec = new Vector<TopeClient>();
        Cursor cursor = null;
        try {
            cursor = database.query(CLIENT_TABLE_NAME, allColumns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TopeClient client = cursorToClient(cursor);
                client.setContext(context);
                vec.add(client);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "ClientDataSource.getAll(): " + e.getMessage());
        }
        return vec;
    }

    /**
     * Return all clients which are marked as active.
     * This clients will be sending the requests the their respective servers.
     *
     * @return
     */
    public Vector<TopeClient> getAllActive() {
        Vector<TopeClient> vec = new Vector<TopeClient>();
        Cursor cursor = null;
        try {
            cursor = database.query(CLIENT_TABLE_NAME, allColumns, CLIENT_ACTIVE + "= '1'", null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TopeClient client = cursorToClient(cursor);
                client.setContext(context);
                vec.add(client);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "ClientDataSource.getAllActive(): " + e.getMessage());
        }
        return vec;
    }

    /**
     * Return all clients which are <b> marked as active and support this method </b>.
     * This clients will be sending the requests the their respective servers.
     *
     * @return
     */
    public Vector<TopeClient> getAllActive(String method) {
        Vector<TopeClient> vec = new Vector<TopeClient>();
        Cursor cursor = innerJoin(method);
        if (null != cursor) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                TopeClient client = cursorToClient(cursor);
                client.setContext(context);
                vec.add(client);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return vec;
    }

    private Cursor innerJoin(String method) {
        String sql = "SELECT " + AldiStringUtils.arrayToString(allColumns, "c.", ", ") + " FROM " + CLIENT_TABLE_NAME + " AS c INNER JOIN " + ACTION_TABLE_NAME + " AS a ON c." + ClientOpenHelper.CLIENT_OWN_ID + "=a."
                + ActionOpenHelper.CLIENT_ID + " WHERE a." + METHOD + "='" + method + "' AND c." + CLIENT_ACTIVE + "= '1';";

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
        } catch (Exception e) {
            Log.e(TAG, "ClientDataSource.innerJoin(): " + e.getMessage());
        }
        return cursor;
    }

    /**
     * Get client by ID.
     *
     * @param id
     * @return
     */
    public TopeClient getClient(long id) {
        Cursor cursor = null;
        try {
            cursor = database.query(CLIENT_TABLE_NAME, allColumns, ClientOpenHelper.CLIENT_OWN_ID + "= ?", new String[]{String.valueOf(id)}, null, null, null);
        } catch (Exception e) {
            Log.e(TAG, "ClientDataSource.getClient(): " + e.getMessage());
        }
        cursor.moveToFirst();

        TopeClient client = cursorToClient(cursor);
        client.setContext(context);

        cursor.close();

        return client;
    }

    /**
     * Delete client from the database.
     *
     * @param client
     */
    public void deleteClient(TopeClient client) {
        long id = client.getId();
        i(TAG, "ClientDataSource.deleteClient(): Comment deleted with id: " + id);
        try {
            database.delete(CLIENT_TABLE_NAME, ClientOpenHelper.CLIENT_OWN_ID + " = " + id, null);
        } catch (Exception e) {
            Log.e(TAG, "ClientDataSource.deleteClient(): " + e.getMessage());
        }
    }

    /**
     * Update the client with the new client representation.
     *
     * @param client
     */
    public boolean updateClient(TopeClient client) {
        long id = client.getId();
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, client.getName());
        values.put(CLIENT_IP, client.getIp());
        values.put(CLIENT_PORT, client.getPort());
        values.put(CLIENT_USER, client.getUser());
        values.put(CLIENT_PASS, client.getPass());
        values.put(CLIENT_ACTIVE, client.isActive());
        values.put(CLIENT_DOMAIN, client.getDomain());
        values.put(CLIENT_MAC, client.getMac());

        int rowsAffected = 0;
        try {
            rowsAffected = database.update(CLIENT_TABLE_NAME, values, ClientOpenHelper.CLIENT_OWN_ID + " = ?", new String[]{String.valueOf(id)});
            if (0 == rowsAffected) {
                e(TAG, "You shouldn't be updating id that aren't there " + client);
            }
        } catch (Exception e) {
            e(TAG, "ClientDataSource.updateClient(): " + e.getMessage());
        }
        return 0 != rowsAffected;
    }

    /**
     * Transforming a cursor data set into a client by matching the fields.
     *
     * @param cursor
     * @return
     */
    private TopeClient cursorToClient(Cursor cursor) {
        TopeClient client = new TopeClient();
        client.setId(cursor.getLong(0));
        client.setName(cursor.getString(1));
        client.setIp(cursor.getString(2));
        client.setPort(cursor.getString(3));
        client.setUser(cursor.getString(4));
        client.setPass(cursor.getString(5));
        client.setActive(cursor.getInt(6) == 1);
        client.setDomain(cursor.getString(7));
        client.setMac(cursor.getString(8));

        return client;
    }

}

package al.aldi.tope.model.db;

import al.aldi.tope.model.TopeClient;
import al.aldi.libjaldi.string.AldiStringUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

import static al.aldi.tope.model.db.ActionOpenHelper.ACTION_TABLE_NAME;
import static al.aldi.tope.model.db.ActionOpenHelper.METHOD;
import static al.aldi.tope.model.db.ClientOpenHelper.*;

/**
 * A wrapper class for accessing tope clients from android inbuilt database.
 *
 * @author Aldi Alimucaj
 *
 */
public class ClientDataSource {
    // Database fields
    private SQLiteDatabase   database;
    private ClientOpenHelper dbClientHelper;
    private String[]         allColumns = { CLIENT_OWN_ID, CLIENT_NAME, CLIENT_IP, CLIENT_PORT, CLIENT_USER, CLIENT_PASS, CLIENT_ACTIVE, CLIENT_DOMAIN, CLIENT_MAC };
    private Context          context;

    static ClientDataSource  instance;

    public ClientDataSource(Context context) {
        super();
        this.context = context;
        this.dbClientHelper = new ClientOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbClientHelper.getWritableDatabase();
    }

    public void close() {
        dbClientHelper.close();
    }

    public boolean isOpen() {
        return database.isOpen();
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

        long insertId = database.insert(CLIENT_TABLE_NAME, null, values);
        client.setId(insertId);

        return insertId != -1;

    }

    /**
     * Create a client from method parameters.
     *
     * @param name
     * @param ip
     * @param port
     * @return
     */
    public TopeClient create(String name, String ip, String port) {
        TopeClient client = null;
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, name);
        values.put(CLIENT_IP, ip);
        values.put(CLIENT_PORT, port);

        long insertId = database.insert(CLIENT_TABLE_NAME, null, values);

        Cursor cursor = database.query(CLIENT_TABLE_NAME, allColumns, ClientOpenHelper.CLIENT_OWN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        client = cursorToClient(cursor);
        cursor.close();

        return client;

    }

    /**
     * Get all clients.
     *
     * @return
     */
    public Vector<TopeClient> getAll() {
        Vector<TopeClient> vec = new Vector<TopeClient>();
        Cursor cursor = database.query(CLIENT_TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TopeClient client = cursorToClient(cursor);
            client.setContext(context);
            vec.add(client);
            cursor.moveToNext();
        }

        cursor.close();

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
        Cursor cursor = database.query(CLIENT_TABLE_NAME, allColumns, CLIENT_ACTIVE + "= '1'", null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TopeClient client = cursorToClient(cursor);
            client.setContext(context);
            vec.add(client);
            cursor.moveToNext();
        }

        cursor.close();

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
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TopeClient client = cursorToClient(cursor);
            client.setContext(context);
            vec.add(client);
            cursor.moveToNext();
        }

        cursor.close();

        return vec;
    }

    private Cursor innerJoin(String method) {
        String sql = "SELECT " + AldiStringUtils.arrayToString(allColumns, "c.", ", ") + " FROM " + CLIENT_TABLE_NAME + " AS c INNER JOIN " + ACTION_TABLE_NAME + " AS a ON c." + ClientOpenHelper.CLIENT_OWN_ID + "=a."
                + ActionOpenHelper.CLIENT_ID + " WHERE a." + METHOD + "='" + method + "' AND c." + CLIENT_ACTIVE + "= '1';";
        System.out.println(sql);
        return database.rawQuery(sql, null);
    }

    /**
     * Get client by ID.
     *
     * @param id
     * @return
     */
    public TopeClient getClient(long id) {
        Cursor cursor = database.query(CLIENT_TABLE_NAME, allColumns, ClientOpenHelper.CLIENT_OWN_ID + "= ?", new String[] { String.valueOf(id) }, null, null, null);
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
        System.out.println("Comment deleted with id: " + id);
        database.delete(CLIENT_TABLE_NAME, ClientOpenHelper.CLIENT_OWN_ID + " = " + id, null);
    }

    /**
     * Update the client with the new client representation.
     *
     * @param client
     */
    public void updateClient(TopeClient client) {
        long id = client.getId();
        ContentValues values = new ContentValues();
        // values.put(CLIENT_ID, id);
        values.put(CLIENT_NAME, client.getName());
        values.put(CLIENT_IP, client.getIp());
        values.put(CLIENT_PORT, client.getPort());
        values.put(CLIENT_USER, client.getUser());
        values.put(CLIENT_PASS, client.getPass());
        values.put(CLIENT_ACTIVE, client.isActive());
        values.put(CLIENT_DOMAIN, client.getDomain());
        values.put(CLIENT_MAC, client.getMac());

        database.update(CLIENT_TABLE_NAME, values, ClientOpenHelper.CLIENT_OWN_ID + " = ?", new String[] { String.valueOf(id) });
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

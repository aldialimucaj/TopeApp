package al.aldi.tope.model.db;

import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_ACTIVE;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_ID;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_IP;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_NAME;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_PASS;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_PORT;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_TABLE_NAME;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_USER;

import java.util.Vector;

import al.aldi.tope.model.TopeClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ClientDataSource {
    // Database fields
    private SQLiteDatabase		database;
    private ClientOpenHelper	dbClientHelper;
    private String[]			allColumns	= { CLIENT_ID, CLIENT_NAME, CLIENT_IP, CLIENT_PORT, CLIENT_USER, CLIENT_PASS, CLIENT_ACTIVE };
    private Context				context;

    static ClientDataSource		instance;

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

    public TopeClient create(String name, String ip, String port) {
        TopeClient client = null;
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, name);
        values.put(CLIENT_IP, ip);
        values.put(CLIENT_PORT, port);

        long insertId = database.insert(CLIENT_TABLE_NAME, null, values);

        Cursor cursor = database.query(CLIENT_TABLE_NAME, allColumns, CLIENT_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        client = cursorToClient(cursor);
        cursor.close();

        return client;

    }

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

    public TopeClient getClient(long id) {
        Cursor cursor = database.query(CLIENT_TABLE_NAME, allColumns, CLIENT_ID + "= ?", new String[] { String.valueOf(id) }, null, null, null);
        cursor.moveToFirst();

        TopeClient client = cursorToClient(cursor);
        client.setContext(context);

        cursor.close();

        return client;
    }

    public void deleteClient(TopeClient client) {
        long id = client.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(CLIENT_TABLE_NAME, CLIENT_ID + " = " + id, null);
    }

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

        database.update(CLIENT_TABLE_NAME, values, CLIENT_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public TopeClient cursorToClient(Cursor cursor) {
        TopeClient client = new TopeClient();
        client.setId(cursor.getLong(0));
        client.setName(cursor.getString(1));
        client.setIp(cursor.getString(2));
        client.setPort(cursor.getString(3));
        client.setUser(cursor.getString(4));
        client.setPass(cursor.getString(5));
        client.setActive(cursor.getInt(6) == 1);

        return client;
    }

}

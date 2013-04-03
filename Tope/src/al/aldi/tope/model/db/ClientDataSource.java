package al.aldi.tope.model.db;

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
    private String[]			allColumns	= { ClientOpenHelper.CLIENT_ID, ClientOpenHelper.CLIENT_NAME, ClientOpenHelper.CLIENT_IP, ClientOpenHelper.CLIENT_PORT,
            ClientOpenHelper.CLIENT_USER, ClientOpenHelper.CLIENT_PASS, ClientOpenHelper.CLIENT_ACTIVE };

    public ClientDataSource(Context context) {
        super();
        this.dbClientHelper = new ClientOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbClientHelper.getWritableDatabase();
    }

    public void close() {
        dbClientHelper.close();
    }

    public TopeClient create(String name, String ip, String port) {
        TopeClient client = null;
        ContentValues values = new ContentValues();
        values.put(ClientOpenHelper.CLIENT_NAME, name);
        values.put(ClientOpenHelper.CLIENT_IP, ip);
        values.put(ClientOpenHelper.CLIENT_PORT, port);

        long insertId = database.insert(ClientOpenHelper.CLIENT_TABLE_NAME, null, values);

        Cursor cursor = database.query(ClientOpenHelper.CLIENT_TABLE_NAME, allColumns, ClientOpenHelper.CLIENT_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        client = cursorToClient(cursor);
        cursor.close();

        return client;

    }

    public Vector<TopeClient> getAll() {
        Vector<TopeClient> vec = new Vector<TopeClient>();
        Cursor cursor = database.query(ClientOpenHelper.CLIENT_TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TopeClient clinet = cursorToClient(cursor);
            vec.add(clinet);
            cursor.moveToNext();
        }

        cursor.close();

        return vec;
    }

    public void deleteClient(TopeClient client) {
        long id = client.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(ClientOpenHelper.CLIENT_TABLE_NAME, ClientOpenHelper.CLIENT_ID + " = " + id, null);
    }

    public TopeClient cursorToClient(Cursor cursor) {
        TopeClient client = new TopeClient();
        client.setId(cursor.getLong(0));
        client.setName(cursor.getString(1));
        client.setIp(cursor.getString(2));
        client.setPort(cursor.getString(3));
        return client;
    }

}

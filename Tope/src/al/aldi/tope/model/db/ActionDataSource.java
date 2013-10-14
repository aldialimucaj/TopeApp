package al.aldi.tope.model.db;

import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static al.aldi.tope.model.db.ActionOpenHelper.*;
import static al.aldi.tope.model.db.ClientOpenHelper.CLIENT_TABLE_NAME;

/**
 * Helper class to access the Database with Action context.
 *
 * @author Aldi Alimucaj
 */
public class ActionDataSource {
    private SQLiteDatabase   database;
    private ActionOpenHelper dbActionHelper;
    private String[] allColumns = {ACTION_OWN_ID, CLIENT_ID, ITEM_ID, MODULE, METHOD, COMMAND_FULL, TITLE, ACTIVE, REVISION_ID, OPPOSITE_ACTION, CONFIRMATION_NEEDED};
    private Context context;

    public ActionDataSource(Context context) {
        super();
        this.context = context;
        this.dbActionHelper = new ActionOpenHelper(context);
        database = dbActionHelper.getWritableDatabase();
    }

    public void open() throws SQLException {
        database = dbActionHelper.getWritableDatabase();
    }

    public void close() {
        if (isOpen()) {
            dbActionHelper.close();
        }
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    public boolean create(TopeAction action) {
        ContentValues values = new ContentValues();
        values.put(ACTION_OWN_ID, action.getActionId());
        values.put(CLIENT_ID, action.getClientId());
        values.put(ITEM_ID, action.getItemId());
        values.put(MODULE, action.getModule());
        values.put(METHOD, action.getMethod());
        values.put(COMMAND_FULL, action.getCommandFullPath());
        values.put(TITLE, action.getTitle());
        values.put(ACTIVE, action.isActive());
        values.put(REVISION_ID, action.getRevisionId());
        values.put(OPPOSITE_ACTION, action.getOppositeActionId());
        values.put(CONFIRMATION_NEEDED, action.isConfirmationNeeded());

        long insertId = database.insert(ACTION_TABLE_NAME, null, values);

        return insertId != -1;
    }

    /**
     * Adds all actions from the list.
     *
     * @param actions
     */
    public void addAll(List<TopeAction> actions) {
        database.beginTransaction();
        for (@SuppressWarnings("rawtypes")
            Iterator iterator = actions.iterator(); iterator.hasNext(); ) {
            TopeAction topeAction = (TopeAction) iterator.next();
            create(topeAction);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    /**
     * Returns all Actions.
     *
     * @return
     */
    public Vector<TopeAction> getAll() {
        Vector<TopeAction> vec = new Vector<TopeAction>();
        Cursor cursor = database.query(ACTION_TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TopeAction action = cursorToAction(cursor);
            vec.add(action);
            cursor.moveToNext();
        }

        cursor.close();

        return vec;
    }

    private Vector<TopeAction> getAllWithPrefix(String prefix) {
        Vector<TopeAction> vec = new Vector<TopeAction>();
        Cursor cursor = database.query(ACTION_TABLE_NAME, allColumns, COMMAND_FULL + " LIKE '" + prefix + "%'", null, null, null, ACTION_OWN_ID);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TopeAction action = cursorToAction(cursor);
            vec.add(action);
            cursor.moveToNext();
        }

        cursor.close();

        return vec;
    }

    /**
     * Retunrs all actions from this client list.
     *
     * @param clients
     * @return
     */
    public Vector<TopeAction> getAll(List<TopeClient> clients) {
        Vector<TopeAction> finalVec = new Vector<TopeAction>();
        for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext(); ) {
            TopeClient topeClient = (TopeClient) iterator.next();

            Vector<TopeAction> vec = new Vector<TopeAction>();
            Cursor cursor = database.query(ACTION_TABLE_NAME, allColumns, CLIENT_ID + "=?", new String[]{String.valueOf(topeClient.getId())}, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                TopeAction action = cursorToAction(cursor);
                vec.add(action);
                cursor.moveToNext();
            }

            cursor.close();
        }

        return finalVec;
    }

    /**
     * Get occurrences without prefix. This returns all actions.
     *
     * @return
     */
    public HashMap<TopeAction, Integer> getAllOccurencies() {
        ClientDataSource clientDataSource = new ClientDataSource(context);
        clientDataSource.open();
        Vector<TopeClient> clients = clientDataSource.getAllActive();
        clientDataSource.close();
        return getAllActionOccurrences(clients, null);
    }

    /**
     * Get occurrences from all Active Clients.
     *
     * @param prefix
     * @return
     */
    public HashMap<TopeAction, Integer> getAllOccurencies(String prefix) {
        ClientDataSource clientDataSource = new ClientDataSource(context);
        clientDataSource.open();
        Vector<TopeClient> clients = clientDataSource.getAllActive();
        clientDataSource.close();
        return getAllActionOccurrences(clients, prefix);
    }

    /**
     * Returns all action occurrences from the client list. This method is used
     * to check whether all actions are supported by all active clients.
     * The mapping is done by [action] = occurrence.
     * Where occurrence is the number of supported actions by the clients.
     *
     * @param clients
     * @param prefix
     * @return
     */
    public HashMap<TopeAction, Integer> getAllActionOccurrences(List<TopeClient> clients, String prefix) {
        HashMap<TopeAction, Integer> finalMap = new HashMap<TopeAction, Integer>();
        Vector<TopeAction> actionsVec = getAllWithPrefix(prefix);

        for (Iterator<TopeAction> iterator = actionsVec.iterator(); iterator.hasNext(); ) {
            TopeAction topeAction = (TopeAction) iterator.next();
            String sql = "SELECT COUNT(c.rowid) FROM "
                    + CLIENT_TABLE_NAME + " AS c INNER JOIN " + ACTION_TABLE_NAME
                    + " AS a ON c." + ClientOpenHelper.CLIENT_OWN_ID + "=a." + ActionOpenHelper.CLIENT_ID
                    + " WHERE a." + METHOD + "='" + topeAction.getMethod() + "'"
                    + " AND a." + CLIENT_ID + " IN (" + AldiStringUtils.arrayToString(getClientIds(clients), ",") + ")"
                    + " ORDER BY " + ACTION_OWN_ID
                    + " ;";

            //System.out.println(sql);

            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                finalMap.put(topeAction, cursor.getInt(0));
                cursor.moveToNext();
            }

            cursor.close();
        }

        return finalMap;
    }

    private String[] getClientIds(List<TopeClient> clients) {
        String[] ids = new String[clients.size()];
        int index = 0;
        for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext(); ) {
            TopeClient topeClient = (TopeClient) iterator.next();
            ids[index++] = topeClient.getId() + "";
        }
        return ids;
    }

    public void dropTable() {
        dbActionHelper.dropTable(database);
    }

    public void createTable() {
        dbActionHelper.createTable(database);
    }

    public void delete(int id) {
        dbActionHelper.delete(database, id);
    }

    public void deleteAll() {
        dbActionHelper.deleteAll(database);
    }

    /**
     * Transforming a cursor data set into a client by matching the fields.
     *
     * @param cursor
     * @return
     */
    private TopeAction cursorToAction(Cursor cursor) {
        TopeAction action = new TopeAction();
        action.setActionId(cursor.getLong(0));
        action.setClientId(cursor.getInt(1));
        action.setItemId(Integer.valueOf(cursor.getString(2)));
        action.setModule(cursor.getString(3));
        action.setMethod(cursor.getString(4));
        action.setCommandFullPath(cursor.getString(5));
        action.setTitle(cursor.getString(6));
        action.setActive(cursor.getInt(7) == 1);
        action.setRevisionId(Integer.valueOf(cursor.getString(8)));
        action.setOppositeActionId(cursor.getLong(9));
        action.setConfirmationNeeded(cursor.getInt(10) == 1);

        return action;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}

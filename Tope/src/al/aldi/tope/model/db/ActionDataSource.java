package al.aldi.tope.model.db;

import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.model.ITopeAction;
import al.aldi.tope.model.TopeAction;
import al.aldi.tope.model.TopeClient;
import al.aldi.tope.utils.TopeUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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

    /**
     * Create new ActionDataSource objects which handles the connection with database
     * to persist Action objects.
     *
     * @param context Activity context required to create the object
     */
    public ActionDataSource(Context context) {
        super();
        this.context = context;
        this.dbActionHelper = new ActionOpenHelper(context);
        open();
    }

    /**
     * Create database handler
     *
     * @throws SQLException
     */
    private void open() throws SQLException {
        if (!isOpen()) {
            try {
                database = dbActionHelper.getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Close Database Handler.
     */
    private void close() {
        if (isOpen()) {
            try {
                dbActionHelper.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
     * Inserts an Action object to the database.
     *
     * @param action which is going to be persited
     * @return true if everything goes ok
     */
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

        database.beginTransaction();
        long insertId = database.insert(ACTION_TABLE_NAME, null, values);
        database.setTransactionSuccessful();
        database.endTransaction();

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
        try {
            Cursor cursor = database.query(ACTION_TABLE_NAME, allColumns, null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                TopeAction action = cursorToAction(cursor);
                vec.add(action);
                cursor.moveToNext();
            }

            cursor.close();
        } catch (SQLiteException e) {
            Log.e("ActionDataSource", e.getMessage());
        }
        return vec;
    }

    /**
     * Return all Actions which belong to a groupe which can be given in as a prefix.
     *
     * @param prefix
     * @return
     */
    private Vector<TopeAction> getAllWithPrefix(String prefix) {
        Vector<TopeAction> vec = new Vector<TopeAction>();
        try {
            Cursor cursor = database.query(ACTION_TABLE_NAME, allColumns, COMMAND_FULL + " LIKE '" + prefix + "%'", null, null, null, ACTION_OWN_ID);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                TopeAction action = cursorToAction(cursor);
                vec.add(action);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e("ActionDataSource", e.getMessage());
        }

        return vec;
    }

    /**
     * Returns Action with specific command.
     *
     * @param command
     * @return
     */
    public ITopeAction getAction(String command) {
        ITopeAction topeAction = null;
        try {
            Cursor cursor = database.query(ACTION_TABLE_NAME, allColumns, COMMAND_FULL + " = '" + command + "'", null, null, null, ACTION_OWN_ID);
            cursor.moveToFirst();

            // should return only one actually
            while (!cursor.isAfterLast()) {
                topeAction = cursorToAction(cursor);
                cursor.moveToLast();
                break;// just to make sure it exits the loop
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e("ActionDataSource ", e.getMessage());
        }

        return topeAction;
    }

    /**
     * Retunrs all actions from this client list.
     *
     * @param clients
     * @return
     */
    public Vector<TopeAction> getAll(List<TopeClient> clients) {
        Vector<TopeAction> finalVec = new Vector<TopeAction>();
        try {
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

        } catch (SQLiteException e) {
            Log.e("ActionDataSource", e.getMessage());
        }

        return finalVec;
    }

    /**
     * Get occurrences without prefix. This returns all actions.
     *
     * @return
     */
    public HashMap<TopeAction, Integer> getAllOccurencies() {
        ClientDataSource clientDataSource = TopeUtils.getClientDataSource(context);
        Vector<TopeClient> clients = clientDataSource.getAllActive();
        return getAllActionOccurrences(clients, null);
    }

    /**
     * Get occurrences from all Active Clients.
     *
     * @param prefix
     * @return
     */
    public HashMap<TopeAction, Integer> getAllOccurencies(String prefix) {
        ClientDataSource clientDataSource = TopeUtils.getClientDataSource(context);
        Vector<TopeClient> clients = clientDataSource.getAllActive();
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

        try {
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
        } catch (SQLiteException e) {
            Log.e("ActionDataSource", e.getMessage());
        }

        return finalMap;
    }

    private String[] getClientIds(List<TopeClient> clients) {
        String[] ids = new String[clients.size()];
        int index = 0;
        for (Iterator<TopeClient> iterator = clients.iterator(); iterator.hasNext(); ) {
            TopeClient topeClient = (TopeClient) iterator.next();
            ids[index++] = String.valueOf(topeClient.getId());
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

    public void deleteFromClientId(int id) {
        dbActionHelper.deleteFromClientId(database, id);
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

    /**
     * Gets the context given in the in constructor or setter.
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     * Set a different context object.
     *
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

}

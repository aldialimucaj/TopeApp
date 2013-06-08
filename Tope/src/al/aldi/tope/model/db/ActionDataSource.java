package al.aldi.tope.model.db;

import static al.aldi.tope.model.db.ActionOpenHelper.*;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import al.aldi.tope.model.TopeAction;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ActionDataSource {
    private SQLiteDatabase          database;
    private ActionOpenHelper        dbActionHelper;
    private String[]                allColumns = {ID, CLIENT_ID, ITEM_ID, MODULE, METHOD, COMMAND_FULL, TITLE, ACTIVE, REVISION_ID, OPPOSITE_ACTION };
    private Context                 context;

    private static ActionDataSource instance;

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
        dbActionHelper.close();
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    public boolean create(TopeAction action) {
        ContentValues values = new ContentValues();
        values.put(ID, action.getActionId());
        values.put(CLIENT_ID, action.getItemId());
        values.put(ITEM_ID, action.getItemId());
        values.put(MODULE, action.getModule());
        values.put(METHOD, action.getMethod());
        values.put(COMMAND_FULL, action.getCommandFullPath());
        values.put(TITLE, action.getTitle());
        values.put(ACTIVE, action.isActive());
        values.put(REVISION_ID, action.getRevisionId());
        values.put(OPPOSITE_ACTION, action.getOppositeActionId());

        long insertId = database.insert(TABLE_NAME, null, values);

        return insertId != -1;
    }

    public void addAll(List<TopeAction> actions){
        for (Iterator iterator = actions.iterator(); iterator.hasNext();) {
            TopeAction topeAction = (TopeAction) iterator.next();
            create(topeAction);
        }
    }

    public Vector<TopeAction> getAll() {
        Vector<TopeAction> vec = new Vector<TopeAction>();
        Cursor cursor = database.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TopeAction action = cursorToAction(cursor);
            vec.add(action);
            cursor.moveToNext();
        }

        cursor.close();

        return vec;
    }

    public void dropTable() {
        dbActionHelper.dropTable(database);
    }

    public void createTable() {
        dbActionHelper.createTable(database);
    }

    /**
     * Transforming a cursor data set into a client by matching the fields.
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

        return action;
    }


}

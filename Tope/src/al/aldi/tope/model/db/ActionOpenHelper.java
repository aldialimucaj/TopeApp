package al.aldi.tope.model.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActionOpenHelper extends SQLiteOpenHelper {
    public static final String TAG = "al.aldi.tope.model.db.ActionOpenHelper";

    private static final int    DATABASE_VERSION    = 1;
    private static final String DATABASE_NAME       = "TopeDatabase.db";
    public static String        ACTION_TABLE_NAME   = "tope_action";
    public static String        ACTION_OWN_ID       = "actionId";
    public static String        CLIENT_ID           = "clientId";
    public static String        ITEM_ID             = "itemId";
    public static String        MODULE              = "module";
    public static String        METHOD              = "method";
    public static String        COMMAND_FULL        = "commandFullPath";
    public static String        TITLE               = "title";
    public static String        ACTIVE              = "active";
    public static String        REVISION_ID         = "revisionId";
    public static String        OPPOSITE_ACTION     = "oppositeActionId";
    public static String        CONFIRMATION_NEEDED = "confirmationNeeded";

    public static String     DB_CREATE_TABLE  = "CREATE TABLE IF NOT EXISTS " + ACTION_TABLE_NAME + " (" +
            ACTION_OWN_ID           + " integer not null, " +
            CLIENT_ID               + " integer, " +
            ITEM_ID                 + " integer, " +
            METHOD                  + " varchar(100), " +
            COMMAND_FULL            + " varchar(100), " +
            MODULE                  + " varchar(255), " +
            TITLE                   + " varchar(100), " +
            ACTIVE                  + " varchar(100), " +
            REVISION_ID             + " integer, "+
            OPPOSITE_ACTION         + " integer, " +
            CONFIRMATION_NEEDED     + " integer, " +
            "UNIQUE ("+CLIENT_ID+","+COMMAND_FULL+"))" // last one, remove comma
                                              ;

    public static String        DB_DROP_TABLE     = "DROP TABLE IF EXISTS " + ACTION_TABLE_NAME;
    public static String        DB_DLETE_WITH_ID  = "DELETE FROM " + ACTION_TABLE_NAME + " WHERE " + CLIENT_ID + " = ?;";
    public static String        DB_DLETE_ALL      = "DELETE FROM " + ACTION_TABLE_NAME + ";";

    public ActionOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DB_CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "INFO: NEW DB-Table CREATED: " + ACTION_TABLE_NAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ClientOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        try {
            db.execSQL(DB_DROP_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(db);
    }

    public void dropTable(SQLiteDatabase db){
        try {
            db.execSQL(DB_DROP_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void createTable(SQLiteDatabase db){
        try {
            db.execSQL(DB_CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void delete(SQLiteDatabase db, int id){
        try {
            db.execSQL(DB_DLETE_WITH_ID, new Integer[] {id});
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteFromClientId(SQLiteDatabase db, int id){
        try {
            db.execSQL(DB_DLETE_WITH_ID, new Integer[] {id});
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    public void deleteAll(SQLiteDatabase db){
        try {
            db.execSQL(DB_DLETE_ALL);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

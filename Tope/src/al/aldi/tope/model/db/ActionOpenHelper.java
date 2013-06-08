package al.aldi.tope.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActionOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static String     TABLE_NAME       = "TOPEACTIONS";
    public static String     ID               = "actionId";
    public static String     CLIENT_ID        = "clientId";
    public static String     ITEM_ID          = "itemId";
    public static String     MODULE           = "module";
    public static String     METHOD           = "method";
    public static String     COMMAND_FULL     = "commandFullPath";
    public static String     TITLE            = "title";
    public static String     ACTIVE           = "active";
    public static String     REVISION_ID      = "revisionId";
    public static String     OPPOSITE_ACTION  = "oppositeActionId";

    public static String     DB_CREATE_TABLE  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            ID + " integer not null primary key autoincrement, " +
            CLIENT_ID + " integer, " +
            ITEM_ID + " integer, " +
            METHOD + " varchar(100), " +
            COMMAND_FULL + " varchar(100), " +
            MODULE + " varchar(255), " +
            TITLE + " varchar(100), " +
            ACTIVE + " varchar(100), " +
            REVISION_ID + " integer, "+
            OPPOSITE_ACTION + " integer )" // last one, remove comma
                                              ;

    public static String     DB_DROP_TABLE    = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ActionOpenHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE);
        System.out.println("INFO: NEW DB-Table CREATED: " + DB_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ClientOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL(DB_DROP_TABLE);
        onCreate(db);

    }

    public void dropTable(SQLiteDatabase db){
        db.execSQL(DB_DROP_TABLE);
    }

    public void createTable(SQLiteDatabase db){
        db.execSQL(DB_CREATE_TABLE);
    }

}

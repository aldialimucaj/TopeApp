package al.aldi.tope.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Low level SQL calls for the ClientDataSource
 * 
 * @author Aldi Alimucaj
 * 
 */
public class ClientOpenHelper extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION  = 1;
    private static final String DATABASE_NAME     = "TopeDatabase.db";
    public static final String  CLIENT_TABLE_NAME = "tope_client";
    public static final String  CLIENT_OWN_ID     = "rowid";
    public static final String  CLIENT_NAME       = "name";
    public static final String  CLIENT_IP         = "ip";
    public static final String  CLIENT_PORT       = "port";
    public static final String  CLIENT_ACTIVE     = "active";
    public static final String  CLIENT_USER       = "user";
    public static final String  CLIENT_PASS       = "pass";
    public static final String  CLIENT_DOMAIN     = "domain";
    public static final String  CLIENT_MAC        = "mac";

    public static final String  DB_CREATE_TABLE   = "CREATE TABLE " + CLIENT_TABLE_NAME + " (" 
    + CLIENT_NAME + " TEXT, " 
    + CLIENT_IP + " TEXT, " 
    + CLIENT_PORT + " TEXT, " 
    + CLIENT_ACTIVE + " TEXT, " 
    + CLIENT_USER + " TEXT, " 
    + CLIENT_PASS + " TEXT, " 
    + CLIENT_DOMAIN + " TEXT, "
    + CLIENT_MAC + " TEXT "
    + ");";

    public ClientOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE);
        System.out.println("INFO: NEW DB CREATED: " + CLIENT_TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ClientOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE_NAME);
        onCreate(db);
    }

}

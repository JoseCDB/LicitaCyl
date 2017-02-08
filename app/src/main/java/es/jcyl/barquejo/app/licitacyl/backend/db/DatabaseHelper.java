package es.jcyl.barquejo.app.licitacyl.backend.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "DB_LICI";

    private static DBTable[] tables = {
            new DBTableLicitaciones(),
            new DBTableRefreshInfo()
    };

    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DBTable table: tables) {
            table.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (DBTable table: tables) {
            table.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
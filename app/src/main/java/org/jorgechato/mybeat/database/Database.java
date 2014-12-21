package org.jorgechato.mybeat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jorgechato.mybeat.util.Constant;

import java.sql.Date;

/**
 * Created by jorge on 11/12/14.
 */
public class Database extends SQLiteOpenHelper implements Constant{
    private static final String DATABASE_NAME = "mybeatdiabetes.db";
    private static final int DATABATE_V = 1;

    private static String[] FROM_CURSOR = {_ID, PATH, NAME, UNITS, DATE, WEIGHT, HEIGHT };

    public Database(Context context) {
        super(context,DATABASE_NAME, null, DATABATE_V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PATH
                + " TEXT NOT NULL, " + NAME + " TEXT NOT NULL," +
                UNITS + " TEXT DEFAULT 'mg/dl'," + DATE + " DATE DEFAULT CURRENTDATE," +
                WEIGHT + " REAL DEFAULT 0," + HEIGHT + " INTEGER DEFAULT 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public Cursor getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Lanza una consulta sobre la base de datos con claúsula FROM y ORDER BY
        Cursor cursor = db.query(TABLE, FROM_CURSOR, null, null, null, null, null);

        return cursor;
    }

    public void newUserData(String path, String name, String unit, Date date, float weight, int heidht){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PATH, path);
        values.put(NAME, name);
        values.put(UNITS, unit);
        values.put(DATE, date.toString());
        values.put(WEIGHT, weight);
        values.put(HEIGHT, heidht);

        db.insertOrThrow(TABLE, null, values);
    }

    public void changeUserData(String path, String name, String unit, Date date, float weight, int heidht){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PATH, path);
        values.put(NAME, name);
        values.put(UNITS, unit);
        values.put(DATE, date.toString());
        values.put(WEIGHT, weight);
        values.put(HEIGHT, heidht);

        db.update(TABLE, values, _ID + " = 1", null);
    }
}

package org.jorgechato.mybeat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jorgechato.mybeat.base.Control;
import org.jorgechato.mybeat.util.Constant;

import java.sql.Date;

/**
 * Created by jorge on 11/12/14.
 */
public class Database extends SQLiteOpenHelper implements Constant{
    private static final String DATABASE_NAME = "mybeatdiabetes.db";
    private static final int DATABATE_V = 2;

    private static String ORDER_BY = DATE + " DESC";
    private static String ORDER_BY_CONTROL ="DATETIME("+ DATEC +","+ TIME + ") DESC";

    private static String[] FROM_CURSOR = {_ID, PATH, NAME, UNITS, DATE, WEIGHT, HEIGHT };
    private static String[] FROM_CURSOR_CONTROL = {_ID, DATEC, TIME, GLUCOSE, NOTE, INSULIN, DAYTIME };

    public Database(Context context) {
        super(context,DATABASE_NAME, null, DATABATE_V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PATH
                + " TEXT NOT NULL, " + NAME + " TEXT NOT NULL," +
                UNITS + " TEXT DEFAULT 'mg/dl'," + DATE + " DATE DEFAULT CURRENT_DATE," +
                WEIGHT + " REAL DEFAULT 0," + HEIGHT + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + CONTROL + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATEC
                + " DATE DEFAULT CURRENT_DATE, " + TIME + " TIME DEFAULT CURRENT_TIME," +
                GLUCOSE + " INTEGER DEFAULT 0," + NOTE + " VARCHAR(150)," +
                INSULIN + " INTEGER DEFAULT 0," + DAYTIME + " VARCHAR(150) )");
    }

    public Cursor dayAverage(){
        SQLiteDatabase db = this.getReadableDatabase();

        String SQL = "SELECT AVG(" + GLUCOSE + ")FROM " + CONTROL + " WHERE " + DATEC + " = CURRENT_DATE";
        Cursor cursor = db.rawQuery(SQL, null);

        return cursor;
    }

    public Cursor weekAverage(){
        SQLiteDatabase db = this.getReadableDatabase();

        String SQL = "SELECT AVG(" + GLUCOSE + ")FROM " + CONTROL + " WHERE strftime('%W', " +DATEC + ") = strftime('%W', date('now'))";
        Cursor cursor = db.rawQuery(SQL, null);

        return cursor;
    }

    public Cursor monthAverage(){
        SQLiteDatabase db = this.getReadableDatabase();

        String SQL = "SELECT AVG(" + GLUCOSE + ")FROM " + CONTROL + " WHERE strftime('%m', " +DATEC + ") = strftime('%m', date('now'))";
        Cursor cursor = db.rawQuery(SQL, null);

        return cursor;
    }

    public void deleteControl(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTROL, _ID + " = " + id, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONTROL);
        onCreate(db);
    }

    public Cursor getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE, FROM_CURSOR, null, null, null, null, null);

        return cursor;
    }

    public Cursor getControl() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CONTROL, FROM_CURSOR_CONTROL, null, null, null, null, ORDER_BY_CONTROL);

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

    public void newControl(Control control){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (control.getDate() != null)
            values.put(DATEC, control.getDate().toString());
        if (control.getTime() != null)
            values.put(TIME, control.getTime().toString());
        values.put(GLUCOSE, control.getGlucose());
        values.put(NOTE, control.getNote());
        values.put(INSULIN, control.getInsulin());
        values.put(DAYTIME, control.getDaytime());

        db.insertOrThrow(CONTROL, null, values);
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

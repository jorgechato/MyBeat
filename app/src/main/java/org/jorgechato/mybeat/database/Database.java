package org.jorgechato.mybeat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jorge on 11/12/14.
 */
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "profile.db";
    private static final int DATABATE_V = 1;

    public Database(Context context) {
        super(context,DATABASE_NAME, null, DATABATE_V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {/*
        db.execSQL("CREATE TABLE " + TABLA + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOMBRE
                + " TEXT NOT NULL, " + LATITUD + " REAL DEFAULT 0," +
                LONGITUD + " REAL DEFAULT 0)");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

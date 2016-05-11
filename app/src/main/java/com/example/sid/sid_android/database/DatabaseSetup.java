package com.example.sid.sid_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSetup extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "translator.db";
    public static final String TRANSLATOR_TABLE = "Translator";
    public static final String COMPANY_TABLE = "Company";
    private static DatabaseSetup dbSetupInstance;

    public DatabaseSetup(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSLATOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COMPANY_TABLE);
        onCreate(db);
    }

    public static DatabaseSetup getInstance(Context context){
        if(dbSetupInstance==null){
            dbSetupInstance=new DatabaseSetup(context);
        }
        return dbSetupInstance;
    }

    public void createDatabase(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS"+ TRANSLATOR_TABLE+"(Username VARCHAR,Password VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS"+ COMPANY_TABLE+"(Username VARCHAR,Password VARCHAR)");
    }

}


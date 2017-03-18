package com.example.sam.mortgagecalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by sam on 3/16/17.
 */

public class MortgageHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Mortgage.db";
    com.example.sam.mortgagecalculator.MortgageSchema mortgageSchema;

    public MortgageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mortgageSchema = new com.example.sam.mortgagecalculator.MortgageSchema();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mortgageSchema.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(mortgageSchema.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}

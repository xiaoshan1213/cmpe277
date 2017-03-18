package com.example.sam.mortgagecalculator;
import android.provider.BaseColumns;

/**
 * Created by sam on 3/16/17.
 */

public final class MortgageSchema {


    public static class MortgageEntry implements BaseColumns {
        public static final String TABLE_NAME = "MORTGAGE";
        public static final String COLUMN_PROPERTY_TYPE = "PROPERTY_TYPE";
        public static final String COLUMN_STREET = "STREET";
        public static final String COLUMN_CITY = "CITY";
        public static final String COLUMN_STATE = "STATE";
        public static final String COLUMN_ZIPCODE = "ZIPCODE";
        public static final String COLUMN_LOAN = "LOAN";
        public static final String COLUMN_LATITUDE = "LATITUDE";
        public static final String COLUMN_LONGITUDE = "LONGITUDE";
        public static final String COLUMN_APR = "APR";
        public static final String COLUMN_MONTHLYPAY = "MONTHLYPAY";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MortgageEntry.TABLE_NAME + " (" +
                    MortgageEntry._ID + " INTEGER PRIMARY KEY," +
                    MortgageEntry.COLUMN_PROPERTY_TYPE + " TEXT," +
                    MortgageEntry.COLUMN_STREET + " TEXT," +
                    MortgageEntry.COLUMN_CITY + " TEXT," +
                    MortgageEntry.COLUMN_STATE + " TEXT," +
                    MortgageEntry.COLUMN_ZIPCODE + " TEXT," +
                    MortgageEntry.COLUMN_APR + " TEXT," +
                    MortgageEntry.COLUMN_MONTHLYPAY + " TEXT," +
                    MortgageEntry.COLUMN_LATITUDE + " TEXT," +
                    MortgageEntry.COLUMN_LONGITUDE + " TEXT," +
                    MortgageEntry.COLUMN_LOAN + " TEXT )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MortgageEntry.TABLE_NAME;

}

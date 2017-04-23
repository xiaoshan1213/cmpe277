package com.example.ami.mortgagecalculator;
import android.provider.BaseColumns;

import java.util.Arrays;

/**
 * Created by ami on 3/16/17.
 */

public final class MortgageSchema {

    public static final int ZIP_CODE_MIN = 10000;
    public static final int ZIP_CODE_MAX = 99999;

    public static class MortgageEntry implements BaseColumns {
        public static final String TABLE_NAME = "MORTGAGE";
        public static final String PROPERTY_TYPE = "PROPERTY_TYPE";
        public static final String STREET = "STREET";
        public static final String CITY = "CITY";
        public static final String STATE = "STATE";
        public static final String ZIPCODE = "ZIPCODE";
        public static final String LOAN = "LOAN";
        public static final String LATITUDE = "LATITUDE";
        public static final String LONGITUDE = "LONGITUDE";
        public static final String APR = "APR";
        public static final String MONTHLYPAY = "MONTHLYPAY";
        public static final String PROPERTY_PRICE = "PROPERTY_PRICE";
        public static final String DOWN_PAYMENT = "DOWN_PAYMENT";
        public static final String YEARS = "YEARS";
    }



    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MortgageEntry.TABLE_NAME;

    public static final String[] projection = {
            MortgageEntry._ID,
            MortgageEntry.LATITUDE,
            MortgageEntry.LONGITUDE,
            MortgageEntry.LOAN,
            MortgageEntry.PROPERTY_TYPE,
            MortgageEntry.CITY,
            MortgageEntry.STATE,
            MortgageEntry.STREET,
            MortgageEntry.MONTHLYPAY,
            MortgageEntry.PROPERTY_PRICE,
            MortgageEntry.DOWN_PAYMENT,
            MortgageEntry.APR,
            MortgageEntry.YEARS,
            MortgageEntry.ZIPCODE

    };


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MortgageEntry.TABLE_NAME + " (" +
                    MortgageEntry._ID + " STRING PRIMARY KEY," +
                    MortgageEntry.PROPERTY_TYPE + " TEXT," +
                    MortgageEntry.STREET + " TEXT," +
                    MortgageEntry.CITY + " TEXT," +
                    MortgageEntry.STATE + " TEXT," +
                    MortgageEntry.ZIPCODE + " TEXT," +
                    MortgageEntry.APR + " TEXT," +
                    MortgageEntry.MONTHLYPAY + " TEXT," +
                    MortgageEntry.LATITUDE + " TEXT," +
                    MortgageEntry.LONGITUDE + " TEXT," +
                    MortgageEntry.LOAN + " TEXT,"+
                    MortgageEntry.PROPERTY_PRICE + " TEXT," +
                    MortgageEntry.DOWN_PAYMENT + " TEXT," +
                    MortgageEntry.YEARS + " TEXT )";

    public static final int COLUMN_PROPERTY_TYPE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.PROPERTY_TYPE);
    public static final int COLUMN_STREET_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.STREET);
    public static final int COLUMN_CITY_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.CITY);
    public static final int COLUMN_LATITUDE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.LATITUDE);
    public static final int COLUMN_LONGITUDE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.LONGITUDE);

}

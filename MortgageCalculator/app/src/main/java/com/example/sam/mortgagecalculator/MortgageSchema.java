package com.example.sam.mortgagecalculator;
import android.provider.BaseColumns;

import java.util.Arrays;

/**
 * Created by sam on 3/16/17.
 */

public final class MortgageSchema {

    public static final int ZIP_CODE_MIN = 10000;
    public static final int ZIP_CODE_MAX = 99999;
    public static final int INVALID_YEARS = -1;

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
        public static final String COLUMN_PROPERTY_PRICE = "PROPERTY_PRICE";
        public static final String COLUMN_DOWN_PAYMENT = "DOWN_PAYMENT";
        public static final String COLUMN_YEARS = "YEARS";
    }



    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MortgageEntry.TABLE_NAME;

    public static final String[] projection = {
            MortgageEntry._ID,
            MortgageEntry.COLUMN_LATITUDE,
            MortgageEntry.COLUMN_LONGITUDE,
            MortgageEntry.COLUMN_LOAN,
            MortgageEntry.COLUMN_PROPERTY_TYPE,
            MortgageEntry.COLUMN_CITY,
            MortgageEntry.COLUMN_STATE,
            MortgageEntry.COLUMN_STREET,
            MortgageEntry.COLUMN_MONTHLYPAY,
            MortgageEntry.COLUMN_PROPERTY_PRICE,
            MortgageEntry.COLUMN_DOWN_PAYMENT,
            MortgageEntry.COLUMN_APR,
            MortgageEntry.COLUMN_YEARS,
            MortgageEntry.COLUMN_ZIPCODE

    };


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MortgageEntry.TABLE_NAME + " (" +
                    MortgageEntry._ID + " STRING PRIMARY KEY," +
                    MortgageEntry.COLUMN_PROPERTY_TYPE + " TEXT," +
                    MortgageEntry.COLUMN_STREET + " TEXT," +
                    MortgageEntry.COLUMN_CITY + " TEXT," +
                    MortgageEntry.COLUMN_STATE + " TEXT," +
                    MortgageEntry.COLUMN_ZIPCODE + " TEXT," +
                    MortgageEntry.COLUMN_APR + " TEXT," +
                    MortgageEntry.COLUMN_MONTHLYPAY + " TEXT," +
                    MortgageEntry.COLUMN_LATITUDE + " TEXT," +
                    MortgageEntry.COLUMN_LONGITUDE + " TEXT," +
                    MortgageEntry.COLUMN_LOAN + " TEXT,"+
                    MortgageEntry.COLUMN_PROPERTY_PRICE + " TEXT," +
                    MortgageEntry.COLUMN_DOWN_PAYMENT + " TEXT," +
                    MortgageEntry.COLUMN_YEARS + " TEXT )";

    public static final int COLUMN_PROPERTY_TYPE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_PROPERTY_TYPE);
    public static final int COLUMN_STREET_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_STREET);
    public static final int COLUMN_CITY_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_CITY);
    public static final int COLUMN_STATE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_STATE);
    public static final int COLUMN_ZIPCODE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_ZIPCODE);
    public static final int COLUMN_APR_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_APR);
    public static final int COLUMN_MONTHLYPAY_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_MONTHLYPAY);
    public static final int COLUMN_LATITUDE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_LATITUDE);
    public static final int COLUMN_LONGITUDE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_LONGITUDE);
    public static final int COLUMN_LOAN_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_LOAN);
    public static final int COLUMN_PROPERTY_PRICE_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_PROPERTY_PRICE);
    public static final int COLUMN_DOWN_PAYMENT_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_DOWN_PAYMENT);
    public static final int COLUMN_YEARS_INDEX = Arrays.asList(projection).indexOf(MortgageEntry.COLUMN_YEARS);

}

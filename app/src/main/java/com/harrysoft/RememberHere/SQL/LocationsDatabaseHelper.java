package com.harrysoft.RememberHere.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationsDatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ", ";
    private static final String NOT_NULL = " not null";
    private static final String NULLABLE = " null";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationsDatabaseContract.LocationsTable.TABLE_NAME + " (" +
                    LocationsDatabaseContract.LocationsTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    LocationsDatabaseContract.LocationsTable.COLUMN_NAME_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LATITUDE + FLOAT_TYPE + NOT_NULL + COMMA_SEP +
                    LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LONGITUDE + FLOAT_TYPE + NOT_NULL + COMMA_SEP +
                    LocationsDatabaseContract.LocationsTable.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + NULLABLE + // Any other options for the CREATE command
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationsDatabaseContract.LocationsTable.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RememberHereData.db";
    private static final String TAG = "DBAdapter";

    public LocationsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion
                + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}

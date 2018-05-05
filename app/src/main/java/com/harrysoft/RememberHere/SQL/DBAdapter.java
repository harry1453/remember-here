package com.harrysoft.RememberHere.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

@SuppressWarnings("UnusedReturnValue")
public class DBAdapter {

    private SQLiteDatabase db;
    private final LocationsDatabaseHelper DBHelper;

    public DBAdapter(Context context) {
        DBHelper = new LocationsDatabaseHelper(context);
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long addLocation(String name, double latitude, double longitude, String description)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_NAME, name);
        initialValues.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LATITUDE, latitude);
        initialValues.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LONGITUDE, longitude);
        initialValues.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_DESCRIPTION, description);
        return db.insert(LocationsDatabaseContract.LocationsTable.TABLE_NAME, null, initialValues);
    }

    public boolean removeLocation(long rowId) {
        return db.delete(LocationsDatabaseContract.LocationsTable.TABLE_NAME, LocationsDatabaseContract.LocationsTable.COLUMN_NAME_ID + "=" + rowId, null) > 0;
    }

    public Cursor getAllLocations() {
        return db.query(LocationsDatabaseContract.LocationsTable.TABLE_NAME, new String[]{
                        LocationsDatabaseContract.LocationsTable.COLUMN_NAME_ID,
                        LocationsDatabaseContract.LocationsTable.COLUMN_NAME_NAME,
                        LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LATITUDE,
                        LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LONGITUDE,
                        LocationsDatabaseContract.LocationsTable.COLUMN_NAME_DESCRIPTION},
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getTitle(long rowId) throws SQLException {
        Cursor cursor = db.query(true, LocationsDatabaseContract.LocationsTable.TABLE_NAME, new String[] {
                LocationsDatabaseContract.LocationsTable.COLUMN_NAME_ID,
                LocationsDatabaseContract.LocationsTable.COLUMN_NAME_NAME,
                LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LATITUDE,
                LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LONGITUDE,
                LocationsDatabaseContract.LocationsTable.COLUMN_NAME_DESCRIPTION},
                LocationsDatabaseContract.LocationsTable.COLUMN_NAME_ID + "=" + rowId,
                null,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    
    public boolean updateTitle(long rowId, String name, double latitude, double longitude, String description){
        ContentValues args = new ContentValues();
        args.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_NAME, name);
        args.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LATITUDE, latitude);
        args.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LONGITUDE, longitude);
        args.put(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_DESCRIPTION, description);
        return db.update(LocationsDatabaseContract.LocationsTable.TABLE_NAME, args, LocationsDatabaseContract.LocationsTable.COLUMN_NAME_ID + "=" + rowId, null) > 0;
    }
}

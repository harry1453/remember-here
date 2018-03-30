package com.harrysoft.RememberHere.SQL;

public class LocationsDatabaseContract {

    public LocationsDatabaseContract() {}

    public static abstract class LocationsTable {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE="longitude";
        public static final String COLUMN_NAME_DESCRIPTION="description";
    }
}

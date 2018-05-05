package com.harrysoft.RememberHere.locationlisttools;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.SQL.LocationsDatabaseContract;

public class LocationCursorAdapter extends CursorAdapter {

    private final TextView emptyText;

    @SuppressWarnings("SameParameterValue")
    public LocationCursorAdapter(Context context, Cursor cursor, int flags, TextView textLabel) {
        super(context, cursor, flags);
        emptyText = textLabel;
        applyEmptyTextIfNeeded(cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        applyEmptyTextIfNeeded(cursor);
        return LayoutInflater.from(context).inflate(R.layout.location_template, parent, false);
    }

    @Override
    public Cursor swapCursor(Cursor cursor){
        applyEmptyTextIfNeeded(cursor);

        return super.swapCursor(cursor);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView locationNameView = view.findViewById(R.id.location_name);

        final int locationId = cursor.getInt(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_ID));
        final String locationName = cursor.getString(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_NAME));
        final String locationDescription = cursor.getString(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_DESCRIPTION));

        locationNameView.setText(locationName);

        RelativeLayout menuListing = view.findViewById(R.id.location_list_item);
        menuListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDialog dialog = new LocationDialog(context, locationName, locationDescription, locationId);
                dialog.show();
            }
        });

    }

    private void applyEmptyTextIfNeeded(Cursor cursor){
        if ((cursor != null) && (cursor.getCount() > 0))
        {
            // Cursor contains data
            emptyText.setVisibility(View.GONE);
        } else {
            // Cursor contains NO data
            emptyText.setVisibility(View.VISIBLE);
        }
    }
}

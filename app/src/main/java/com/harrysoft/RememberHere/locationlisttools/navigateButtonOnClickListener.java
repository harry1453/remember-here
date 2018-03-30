package com.harrysoft.RememberHere.locationlisttools;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.SQL.DBAdapter;
import com.harrysoft.RememberHere.SQL.LocationsDatabaseContract;

public abstract class navigateButtonOnClickListener implements View.OnClickListener {
    DBAdapter db;
    private Context context;
    private int id;

    public navigateButtonOnClickListener(Context context, int id){
        this.context = context;
        this.id = id;
        db = new DBAdapter(this.context);
    }

    @Override
    public void onClick(View view) {
        db.open();
        Cursor cursor = db.getTitle(id);
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LONGITUDE));
        String mode = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.settings_default_transport_mode), "w");

        Intent intent;

        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.auto_engage_navigation), false)) {
            //Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + Double.toString(latitude) + "," + Double.toString(longitude)));
            intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + Double.toString(latitude) + "," + Double.toString(longitude) + "&mode=" + mode));
        }
        else {
            intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + Double.toString(latitude) + "," + Double.toString(longitude)));
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.settings_always_use_google_maps), true)){
            intent.setPackage("com.google.android.apps.maps");
        }
        context.startActivity(intent);
    }
}

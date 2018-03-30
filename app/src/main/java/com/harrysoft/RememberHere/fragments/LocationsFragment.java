package com.harrysoft.RememberHere.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.SQL.DBAdapter;
import com.harrysoft.RememberHere.locationlisttools.LocationCursorAdapter;
import com.harrysoft.RememberHere.tools.Constants;

public class LocationsFragment extends Fragment {

    private DBAdapter dbAdapter;
    private Cursor cursor;
    LocationCursorAdapter locationCursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        Context context = view.getContext();
        dbAdapter = new DBAdapter(context);

        if (context.getSharedPreferences(Constants.adSettingsFileName, Context.MODE_PRIVATE).getBoolean(Constants.adsEnabledKey, true))
        {
            // INITIALISE ADVERTS
            AdView adView = view.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        try {
            dbAdapter.open();
            cursor = dbAdapter.getAllLocations();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "FATAL ERROR: Cannot establish database access, invalid context", Toast.LENGTH_LONG).show();
            return view;
        }

        ListView locationsList;
        try {
            TextView text = view.findViewById(R.id.locations_screen_no_locations_label);
            locationsList = view.findViewById(R.id.locations_list);
            locationCursorAdapter = new LocationCursorAdapter(context, cursor, 0, text);
        } catch (NullPointerException e){
            Toast.makeText(getActivity(), "FATAL ERROR: Cannot access view, invalid context", Toast.LENGTH_LONG).show();
            return view;
        }
        locationsList.setAdapter(locationCursorAdapter);

        dbAdapter.close();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        reloadCursor();
    }

    public void reloadCursor(){
        try {
            dbAdapter.open();
            cursor = dbAdapter.getAllLocations();
            locationCursorAdapter.swapCursor(cursor);
            dbAdapter.close();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "FATAL ERROR: Cannot establish database access, invalid context", Toast.LENGTH_LONG).show();
        }
    }
}

package com.harrysoft.RememberHere.locationtools;


import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.harrysoft.RememberHere.R;

abstract class getGpsDialog extends Dialog {

    private final Context context;

    private final LocationManager lm;

    public getGpsDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_gps_dialog);

        this.context = context;

        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Button cancelButton = findViewById(R.id.cancel_gps_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show(){
        super.show();

        Location location;
        try {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e){
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT).show();
            location = null;
            dismiss();
        }

        if (location != null) {
            sendGPSCoordinates(location);
        }

        final LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location newLocation) {
                sendGPSCoordinates(newLocation);
            }

            @Override
            public void onProviderDisabled(String arg0) {

            }

            @Override
            public void onProviderEnabled(String arg0) {

            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

            }
        };

        try {
            lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        } catch (SecurityException e){
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void sendGPSCoordinates(Location location){
        takeDialogGPSCoordinates(location.getLatitude(), location.getLongitude());
    }

    public abstract void takeDialogGPSCoordinates(double latitude, double longitude);
}

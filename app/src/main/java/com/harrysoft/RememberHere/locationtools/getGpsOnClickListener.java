package com.harrysoft.RememberHere.locationtools;

import android.content.Context;
import android.view.View;

public abstract class getGpsOnClickListener implements View.OnClickListener{

    Context context;

    private getGpsDialog gpsDialog;

    public getGpsOnClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v){
        gpsDialog = new getGpsDialog(context) {
            @Override
            public void takeDialogGPSCoordinates(double latitude, double longitude) {
                takeGPSCoordinates(latitude, longitude);
                dismiss();
            }
        };
        gpsDialog.show();
    }

    public abstract void takeGPSCoordinates(double latitude, double longitude);
}

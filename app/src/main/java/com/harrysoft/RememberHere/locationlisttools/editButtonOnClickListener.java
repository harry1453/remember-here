package com.harrysoft.RememberHere.locationlisttools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.harrysoft.RememberHere.activities.EditLocation;

public abstract class editButtonOnClickListener implements OnClickListener {

    private Context context;
    private int id;

    public editButtonOnClickListener(Context context, int id){
        this.context = context;
        this.id = id;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, EditLocation.class);
        intent.putExtra("locationID", id);
        context.startActivity(intent);
        /*while (isEditLocationActivityRunning())
        {
            // Wait
        }
        refresh();*/
    }

    private boolean isEditLocationActivityRunning(){
        ActivityManager manager = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        for (  ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (EditLocation.class.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

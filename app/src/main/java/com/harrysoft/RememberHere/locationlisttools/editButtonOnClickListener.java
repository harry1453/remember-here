package com.harrysoft.RememberHere.locationlisttools;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.harrysoft.RememberHere.activities.EditLocation;

abstract class editButtonOnClickListener implements OnClickListener {

    private final Context context;
    private final int id;

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

}

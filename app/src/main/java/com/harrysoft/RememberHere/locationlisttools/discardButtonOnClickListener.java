package com.harrysoft.RememberHere.locationlisttools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.harrysoft.RememberHere.SQL.DBAdapter;

public abstract class discardButtonOnClickListener implements View.OnClickListener {
    private final DBAdapter db;
    private final Context context;
    private final int DBid;

    public discardButtonOnClickListener(Context context, int id){
        this.context = context;
        this.DBid = id;
        db = new DBAdapter(context);
    }

    @Override
    public void onClick(View view) {
        final boolean[] responseReceived = {false};
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context);
        confirmDialog.setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.open();
                        db.removeLocation(DBid);
                        db.close();
                        Toast.makeText(context, "Location deleted", Toast.LENGTH_SHORT).show();
                        closeActivity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        confirmDialog.show();
    }

    protected abstract void closeActivity();
}

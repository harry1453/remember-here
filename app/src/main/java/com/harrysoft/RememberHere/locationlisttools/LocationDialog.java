package com.harrysoft.RememberHere.locationlisttools;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.harrysoft.RememberHere.R;

public class LocationDialog extends Dialog implements android.view.View.OnClickListener{
    Context c;
    Button navigateButton, editButton, cancelButton;
    String locationName, locationDescription;
    int id;

    public LocationDialog(Context c, String locationName, String locationDescription, int id) {
        super(c);
        this.c = c;
        this.locationName = locationName;
        this.locationDescription = locationDescription;
        this.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.location_menu_dialog);
        navigateButton = (Button) findViewById(R.id.navigateButton);
        editButton = (Button) findViewById(R.id.editButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        navigateButton.setOnClickListener(new navigateButtonOnClickListener(c, this.id) {
            @Override
            public void onClick(View v){
                super.onClick(v);
                dismiss();
            }
        });
        editButton.setOnClickListener(new editButtonOnClickListener(c, this.id) {

            @Override
            public void onClick(View v){
                super.onClick(v);
                dismiss();
            }
        });
        cancelButton.setOnClickListener(this);

        TextView locationTitleText = (TextView) findViewById(R.id.location_title);
        TextView locationDescriptionText = (TextView) findViewById(R.id.location_description);

        if (locationDescription.equals(""))
        {
            locationDescriptionText.setVisibility(View.GONE);
        }

        locationTitleText.setText(locationName);
        locationDescriptionText.setText(locationDescription);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}

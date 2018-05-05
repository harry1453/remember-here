package com.harrysoft.RememberHere.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.SQL.DBAdapter;
import com.harrysoft.RememberHere.SQL.LocationsDatabaseContract;
import com.harrysoft.RememberHere.locationlisttools.discardButtonOnClickListener;
import com.harrysoft.RememberHere.locationtools.getGpsOnClickListener;
import com.harrysoft.RememberHere.tools.Constants;


public class EditLocation extends AppCompatActivity {

    private EditText nameBox;
    private EditText latitudeBox;
    private EditText longitudeBox;
    private EditText descriptionBox;

    private final DBAdapter db = new DBAdapter(this);

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        int intentIDExtra = getIntent().getIntExtra("locationID", -1);
        if (intentIDExtra == -1){
            Toast.makeText(this, "Fatal error: Cannot find location ID in intent", Toast.LENGTH_LONG).show();
            finish();
        }
        id = intentIDExtra;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button useGpsButton = findViewById(R.id.edit_screen_use_gps_button);
        Button saveButton = findViewById(R.id.edit_screen_save_button);
        Button deleteButton = findViewById(R.id.delete_button);
        nameBox = findViewById(R.id.edit_screen_name_text_box);
        latitudeBox = findViewById(R.id.edit_screen_latitude_text_box);
        longitudeBox = findViewById(R.id.edit_screen_longitude_text_box);
        descriptionBox = findViewById(R.id.edit_screen_description_text_box);

        useGpsButton.setOnClickListener(new getGpsOnClickListener(this) {
            @Override
            public void takeGPSCoordinates(double latitude, double longitude) {
                latitudeBox.setText(String.valueOf(latitude));
                longitudeBox.setText(String.valueOf(longitude));
            }
        });

        saveButton.setOnClickListener(view -> saveLocation());

        deleteButton.setOnClickListener(new discardButtonOnClickListener(this, id) {
            @Override
            protected void closeActivity() {
                finish();
            }
        });

        db.open();
        Cursor cursor;

        cursor = db.getTitle(id);

        String initialName = cursor.getString(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_NAME));
        String initialDescription = cursor.getString(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_DESCRIPTION));
        double initialLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LATITUDE));
        double initialLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDatabaseContract.LocationsTable.COLUMN_NAME_LONGITUDE));

        nameBox.setText(initialName);
        descriptionBox.setText(initialDescription);
        latitudeBox.setText(String.valueOf(initialLatitude));
        longitudeBox.setText(String.valueOf(initialLongitude));

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_location, menu);
        return true;
    }

    private void saveLocation(){
        String name = nameBox.getText().toString();
        String latitudeStr = latitudeBox.getText().toString();
        String longitudeStr = longitudeBox.getText().toString();
        String description = descriptionBox.getText().toString();
        if (VerifyTextBoxes(name, latitudeStr, longitudeStr)){
            //Toast.makeText(getApplicationContext(), "Name: " + nameBox.getText().toString() + ", Latitude: " + latitudeBox.getText().toString() + ", Longitude: " + longitudeBox.getText().toString(), Toast.LENGTH_SHORT).show();
            // Gets the data repository in write mode
            double latitude, longitude;
            try {
                latitude = Double.parseDouble(latitudeStr);
            } catch (NumberFormatException e) {
                // String latitude did not contain a valid number
                Toast.makeText(getApplicationContext(), "Error: Unable to parse field Latitude", Toast.LENGTH_LONG).show();
                return; // Cancel button press
            }
            try {
                longitude = Double.parseDouble(longitudeStr);
            } catch (NumberFormatException e) {
                // String latitude did not contain a valid number
                Toast.makeText(getApplicationContext(), "Error: Unable to parse field Longitude", Toast.LENGTH_LONG).show();
                return; // Cancel button press
            }

            db.open();
            db.updateTitle(id, name, latitude, longitude, description);
            db.close();
            if (id != -1){
                Toast.makeText(getApplicationContext(), "Location updated successfully", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "Name: " + name + ", latitude: " + Double.toString(latitude) + ", longitude: " + Double.toString(longitude) + ", Description: " + description + ", id: " + String.valueOf(id), Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Error: Cannot write location to database", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean VerifyTextBoxes(String name, String latitude, String longitude){
        if(TextUtils.isEmpty(name)) {
            nameBox.setError(Constants.textBoxEmptyError);
            return false;
        }
        if(TextUtils.isEmpty(latitude)) {
            latitudeBox.setError(Constants.textBoxEmptyError);
            return false;
        }
        if(TextUtils.isEmpty(longitude)) {
            longitudeBox.setError(Constants.textBoxEmptyError);
            return false;
        }
        // If we got to here they must be full
        return true;
    }
}

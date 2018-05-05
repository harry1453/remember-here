package com.harrysoft.RememberHere.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.SQL.DBAdapter;
import com.harrysoft.RememberHere.locationtools.getGpsOnClickListener;
import com.harrysoft.RememberHere.tools.Constants;


public class NewLocation extends AppCompatActivity {

    private EditText nameBox;
    private EditText latitudeBox;
    private EditText longitudeBox;
    private EditText descriptionBox;

    private final DBAdapter db = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button useGpsButton = findViewById(R.id.use_gps_button);
        Button saveButton = findViewById(R.id.save_button);
        nameBox = findViewById(R.id.name_text_box);
        latitudeBox = findViewById(R.id.latitude_text_box);
        longitudeBox = findViewById(R.id.longitude_text_box);
        descriptionBox = findViewById(R.id.description_text_box);

        useGpsButton.setOnClickListener(new getGpsOnClickListener(this) {
                                            @Override
                                            public void takeGPSCoordinates(double latitude, double longitude) {
                                                latitudeBox.setText(String.valueOf(latitude));
                                                longitudeBox.setText(String.valueOf(longitude));
                                            }
                                        });

        saveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveLocation();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_location, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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

            //float latitude = Float.valueOf(latitudeStr);
            //float longitude = Float.valueOf(longitudeStr);

            db.open();
            long id;
            id = db.addLocation(name, latitude, longitude, description);
            db.close();
            if (id != -1){
                Toast.makeText(getApplicationContext(), "Location added successfully", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "Name: " + name + ", latitude: " + Double.toString(latitude) + ", longitude: " + Double.toString(longitude) + ", Description: " + description + ", id: " + String.valueOf(id), Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Error: Cannot write location to database", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean VerifyTextBoxes(String name, String latitude, String longitude){

        boolean success = true;

        if(TextUtils.isEmpty(name)) {
            nameBox.setError(Constants.textBoxEmptyError);
            success = false;
        }
        if(TextUtils.isEmpty(latitude)) {
            latitudeBox.setError(Constants.textBoxEmptyError);
            success = false;
        }
        if(TextUtils.isEmpty(longitude)) {
            longitudeBox.setError(Constants.textBoxEmptyError);
            success = false;
        }

        return success;
    }
}

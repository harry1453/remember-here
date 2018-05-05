package com.harrysoft.RememberHere.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.fragments.LocationsFragment;
import com.harrysoft.RememberHere.tools.Constants;


public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        FloatingActionButton fab = findViewById(R.id.fab);

        setUpDrawer();

        fab.setOnClickListener(view -> addNewLocation());

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            LocationsFragment firstFragment = new LocationsFragment();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }

        if (getSharedPreferences(Constants.appSettingsFileName, Context.MODE_PRIVATE).getBoolean(Constants.firstLaunchKey, true))
        {
            AlertDialog.Builder instructionsDialog = new AlertDialog.Builder(this);
            instructionsDialog.setMessage(getString(R.string.instructions_message));
            instructionsDialog.setTitle(getString(R.string.instructions_title));
            instructionsDialog.setPositiveButton("OK", (dialog, which) -> {});
            instructionsDialog.setCancelable(true);
            instructionsDialog.create().show();
            SharedPreferences sharedPref = getSharedPreferences(Constants.appSettingsFileName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Constants.firstLaunchKey, false);
            editor.apply();
        }
    }

    private void setFragmentAsLocations(){
        LocationsFragment newFragment = new LocationsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addNewLocation(){
        startActivity(new Intent(this, NewLocation.class));
    }

    private void viewLocations(){
        actionBar.setTitle("My locations");
        setFragmentAsLocations();
    }

    private void openSettings(){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openAboutScreen(){
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }

    private void setUpDrawer(){
        mDrawerList = findViewById(R.id.left_drawer);
        mDrawerLayout = findViewById(R.id.main_screen_drawer_layout);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Constants.drawerItems);
        mDrawerList.setAdapter(mAdapter);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerList.setOnItemClickListener((parent, view, position, id) -> {

            Object o = mDrawerList.getItemAtPosition(position);
            switch (o.toString()) {
                case Constants.drawerOption1:
                    viewLocations();
                    break;
                case Constants.drawerOption2:
                    openAboutScreen();
                    break;
                case Constants.drawerOption3:
                    openSettings();
                    break;
                case Constants.drawerOption4:
                    System.exit(0);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Error: Cannot find selected drawer item", Toast.LENGTH_LONG).show();
            }
            mDrawerLayout.closeDrawers();
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }
}

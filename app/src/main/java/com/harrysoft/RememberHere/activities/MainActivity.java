package com.harrysoft.RememberHere.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.SQL.LocationsDatabaseHelper;
import com.harrysoft.RememberHere.fragments.LocationsFragment;
import com.harrysoft.RememberHere.tools.Constants;


public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private ArrayAdapter<String> mAdapter;
    private ListView mDrawerList;
    private FloatingActionButton fab;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    LocationsDatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mActivityTitle = getTitle().toString();

        mDbHelper = new LocationsDatabaseHelper(this);

        setUpDrawer();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewLocation();
            }
        });

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            LocationsFragment firstFragment = new LocationsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }

        if (getSharedPreferences(Constants.appSettingsFileName, Context.MODE_PRIVATE).getBoolean(Constants.firstLaunchKey, true))
        {
            AlertDialog.Builder instructionsDialog = new AlertDialog.Builder(this);
            instructionsDialog.setMessage(getString(R.string.instructions_message));
            instructionsDialog.setTitle(getString(R.string.instructions_title));
            instructionsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            instructionsDialog.setCancelable(true);
            instructionsDialog.create().show();
            SharedPreferences sharedPref = getSharedPreferences(Constants.appSettingsFileName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Constants.firstLaunchKey, false);
            editor.commit();
        }
    }

    private void setFragmentAsLocations(){
        // Create fragment and give it an argument specifying the article it should show
        LocationsFragment newFragment = new LocationsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void refreshLocations(){
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            /*case R.id.action_refresh_locations:
                refreshLocations();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
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
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.main_screen_drawer_layout);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Constants.drawerItems);
        mDrawerList.setAdapter(mAdapter);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

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
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //actionBar.setTitle(mActivityTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
}

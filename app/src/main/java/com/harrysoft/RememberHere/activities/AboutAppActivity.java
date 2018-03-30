package com.harrysoft.RememberHere.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.tools.Constants;
import com.harrysoft.RememberHere.util.IabHelper;
import com.harrysoft.RememberHere.util.IabResult;
import com.harrysoft.RememberHere.util.Inventory;
import com.harrysoft.RememberHere.util.Purchase;

import me.grantland.widget.AutofitHelper;

public class AboutAppActivity extends AppCompatActivity {

    private static final String ITEM_SKU = "remember_here_remove_adverts";
    private TextView adsLabel;

    IabHelper purchaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        try{
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e)
        {
            Toast.makeText(this, "Error initialising action bar", Toast.LENGTH_LONG).show();
        }

        adsLabel = (TextView) findViewById(R.id.about_screen_ads_enabled_label);

        updateAdsLabel(adsLabel);

        purchaseHelper = new IabHelper(this, Constants.b64PublicKey);

        purchaseHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Toast.makeText(getApplicationContext(), "In-app Billing setup failed: " + result, Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView appNameLabel = (TextView) findViewById(R.id.app_name_label);
        TextView copyrightLabel = (TextView) findViewById(R.id.copyright_label);
        TextView versionLabel = (TextView) findViewById(R.id.version_label);
        AutofitHelper.create(appNameLabel);
        AutofitHelper.create(copyrightLabel);
        AutofitHelper.create(versionLabel);
        AutofitHelper.create(adsLabel);

        Button disableAdsButton = (Button) findViewById(R.id.disable_ads_button);
        Button restorePurchasesButton = (Button) findViewById(R.id.about_screen_restore_purchases_button);
        Button showInstructionsButton = (Button) findViewById(R.id.instructions_button);
        showInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instructionsDialog = new AlertDialog.Builder(AboutAppActivity.this);
                instructionsDialog.setMessage(getString(R.string.instructions_message));
                instructionsDialog.setTitle(getString(R.string.instructions_title));
                instructionsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                instructionsDialog.setCancelable(true);
                instructionsDialog.create().show();
            }
        });
        restorePurchasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restorePurchases();
            }
        });
        disableAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAds(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_app, menu);
        return true;
    }

    public void disableAds(View view){
        purchaseHelper.launchPurchaseFlow(this, ITEM_SKU, 10001, mPurchaseFinishedListener, Constants.removeAdsPurchaseToken);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!purchaseHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                Toast.makeText(getApplicationContext(), "Purchase Failed.", Toast.LENGTH_LONG).show();
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                //consumeItem();
                setAdsAsDisabled();
            }

        }
    };

    private void setAdsAsDisabled(){
        SharedPreferences sharedPref = getSharedPreferences(Constants.adSettingsFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.adsEnabledKey, false);
        editor.apply();
    }

    private void updateAdsLabel(TextView label){
        boolean adsEnabled = getSharedPreferences(Constants.adSettingsFileName, Context.MODE_PRIVATE).getBoolean(Constants.adsEnabledKey, true);
        if (adsEnabled){
            label.setText("Ads enabled");
        } else {
            label.setText("Ads disabled");
        }
    }

    private void restorePurchases(){
        purchaseHelper.queryInventoryAsync(mGotInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // handle error here
            }
            else {
                // does the user have the premium upgrade?
                boolean adsDisabled = inventory.hasPurchase(ITEM_SKU);
                SharedPreferences sharedPref = getSharedPreferences(Constants.adSettingsFileName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(Constants.adsEnabledKey, !adsDisabled);
                editor.apply();
                // update UI accordingly
                updateAdsLabel(adsLabel);
            }
        }
    };
}
package com.harrysoft.RememberHere.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.RememberHere.R;
import com.harrysoft.RememberHere.tools.Constants;
import com.harrysoft.RememberHere.util.IabHelper;
import com.harrysoft.RememberHere.util.IabResult;
import com.harrysoft.RememberHere.util.Inventory;

import me.grantland.widget.AutofitHelper;

public class AboutAppActivity extends AppCompatActivity {

    private static final String ITEM_SKU = "remember_here_remove_adverts";
    private TextView adsLabel;

    private IabHelper purchaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adsLabel = findViewById(R.id.about_screen_ads_enabled_label);

        updateAdsLabel(adsLabel);

        purchaseHelper = new IabHelper(this, Constants.b64PublicKey);

        purchaseHelper.startSetup(result -> {
            if (!result.isSuccess()) {
                Toast.makeText(getApplicationContext(), "In-app Billing setup failed: " + result, Toast.LENGTH_SHORT).show();
            }
        });

        TextView appNameLabel = findViewById(R.id.app_name_label);
        TextView copyrightLabel = findViewById(R.id.copyright_label);
        TextView versionLabel = findViewById(R.id.version_label);
        AutofitHelper.create(appNameLabel);
        AutofitHelper.create(copyrightLabel);
        AutofitHelper.create(versionLabel);
        AutofitHelper.create(adsLabel);

        Button disableAdsButton = findViewById(R.id.disable_ads_button);
        Button restorePurchasesButton = findViewById(R.id.about_screen_restore_purchases_button);
        Button showInstructionsButton = findViewById(R.id.instructions_button);
        showInstructionsButton.setOnClickListener(v -> {
            AlertDialog.Builder instructionsDialog = new AlertDialog.Builder(AboutAppActivity.this);
            instructionsDialog.setMessage(getString(R.string.instructions_message));
            instructionsDialog.setTitle(getString(R.string.instructions_title));
            instructionsDialog.setPositiveButton("OK", (dialog, which) -> {});
            instructionsDialog.setCancelable(true);
            instructionsDialog.create().show();
        });
        restorePurchasesButton.setOnClickListener(v -> restorePurchases());
        disableAdsButton.setOnClickListener(v -> disableAds());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_app, menu);
        return true;
    }

    private void disableAds(){
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

    private final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = (result, purchase) -> {
                if (result.isFailure()) {
                    Toast.makeText(getApplicationContext(), "Purchase Failed.", Toast.LENGTH_LONG).show();
                }
                else if (purchase.getSku().equals(ITEM_SKU)) {
                    //consumeItem();
                    setAdsAsDisabled();
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
            label.setText(R.string.ads_enabled);
        } else {
            label.setText(R.string.ads_disabled);
        }
    }

    private void restorePurchases(){
        purchaseHelper.queryInventoryAsync(mGotInventoryListener);
    }

    private final IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (!result.isFailure()) {
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

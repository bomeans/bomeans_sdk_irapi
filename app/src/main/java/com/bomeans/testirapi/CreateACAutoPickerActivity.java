package com.bomeans.testirapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bomeans.irapi.IACSmartPicker;
import com.bomeans.irapi.ICreateSmartPickerCallback;
import com.bomeans.irapi.IGetSmartPickerKeysCallback;
import com.bomeans.irapi.IIRACSmartPickerCallback;
import com.bomeans.irapi.IRAPI;
import com.bomeans.irapi.ITVSmartPicker;
import com.bomeans.irapi.SmartPickerResult;

import java.util.List;

public class CreateACAutoPickerActivity extends AppCompatActivity {

    private String DBG_TAG = "IRAPI";

    private IACSmartPicker mSmartPicker;

    private Button mTestButton;
    private TextView mInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acauto_picker);

        setTitle("AC Auto Picker Demo");

        // enable the back button on the action bar
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // get parameters
        final String typeId = getIntent().getStringExtra("type_id");
        final String brandId = getIntent().getStringExtra("brand_id");

        mTestButton = (Button) findViewById(R.id.button_key);
        mInfoText = (TextView) findViewById(R.id.info_text);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Button restartButton = (Button) findViewById(R.id.button_restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartAutoSmartPicker();
            }
        });


        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutoPickerMatched();
            }
        });

        /**
         * Get the keys for the smart picker.
         * This is just a helper API in case you need to know the keys for the smart picker
         * before you actually download the smart picker. In most case, you can just get the
         * key sequence in the smart picker.
         * Note: Not all keys are necessary to be presented in the smart picker for a specific type and
         * brand. A smart picker uses only keys needed for helping the picking process.
         */
        IRAPI.getSmartPickerKeys(typeId, getNew(), new IGetSmartPickerKeysCallback() {
            @Override
            public void onDataReceived(List<String> list) {
                Log.d(DBG_TAG, "Key IDs for smart picker:");
                for (String keyId : list) {
                    Log.d(DBG_TAG, keyId);
                }

                IRAPI.createSmartPicker(typeId, brandId, getNew(),
                        new String[] {"IR_KEY_POWER_TOGGLE", "IR_KEY_VOLUME_UP", "IR_KEY_VOLUME_DOWN"},

                        new ICreateSmartPickerCallback() {
                            @Override
                            public void onPickerCreated(ITVSmartPicker smartPicker) {

                                progressBar.setVisibility(View.GONE);

                                if (smartPicker instanceof IACSmartPicker) {
                                    mSmartPicker = (IACSmartPicker) smartPicker;
                                    restartAutoSmartPicker();
                                } else {
                                    Log.e(DBG_TAG, "Failed to create AC smart picker!");
                                }
                            }

                            @Override
                            public void onError(int errorCode) {
                                progressBar.setVisibility(View.GONE);
                                Log.d(DBG_TAG, String.format("ERROR]:%d failed to create smart picker", errorCode));
                            }
                        });
            }

            @Override
            public void onError(int errorCode) {
                Log.d(DBG_TAG, String.format("ERROR]:%d failed to create smart picker", errorCode));
            }
        });
    }

    private Boolean setAutoPickerMatched() {
        if (null == mSmartPicker) {
            return false;
        }

        mSmartPicker.setAutoPickerMatched();

        return true;
    }

    private Boolean restartAutoSmartPicker() {
        if (null == mSmartPicker) {
            return false;
        }

        mSmartPicker.startAutoPicker(new IIRACSmartPickerCallback() {
            @Override
            public void onPickerInfo(int currentIndex, int totalCount, String currentRemoteId) {
                mInfoText.setText(String.format("%s (%d/%d)", currentRemoteId, currentIndex, totalCount));
            }

            @Override
            public void onRemoteMatched(String remoteId) {
                SmartPickerResult[] results = mSmartPicker.getPickerResult();
                String msg = "Matched Remote: ";
                for (SmartPickerResult result : results) {
                    msg += String.format("Remote ID = %s (Type Id = %s, Brand Id = %s)",
                            result.remoteId, result.typeId, result.brandId);
                }

                mInfoText.setText(msg);
            }

            @Override
            public void onRemoteMatchFailed() {
                mInfoText.setText("No Matched!");
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean getNew() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPref.getBoolean("get_new", false);
    }

    @Override
    protected void onPause() {

        if (null != mSmartPicker) {
            mSmartPicker.endAutoPicker();
        }
        super.onPause();
    }
}

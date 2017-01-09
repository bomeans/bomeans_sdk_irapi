package com.bomeans.testirapi;

import android.app.Application;

import com.bomeans.irapi.IRAPI;

/**
 * Created by ray on 2016/11/20.
 */

public class TestIRAPIApp extends Application {

    // apply a API KEY from Bomeans to run this demo
    public static String BOMEANS_SDK_API_KEY = "";

    private MyIrBlaster mMyIrBlaster = new MyIrBlaster();

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize SDK
        initializeSDK();
    }

    private void initializeSDK() {

        // initialize the SDK
        IRAPI.init(BOMEANS_SDK_API_KEY, getApplicationContext());

        // select server if needed
        IRAPI.switchToChineseServer(false);

        // set up the IR Blaster hardware data handling
        IRAPI.setCustomerIrBlaster(mMyIrBlaster);

    }

    public MyIrBlaster getMyIrBlaster() {
        return mMyIrBlaster;
    }

}

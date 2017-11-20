package com.bomeans.irapi;

/**
 * Created by ray on 2017/11/3.
 */

public interface IGetFirmwareVersionCallback {

    void onCompleted(String versionString);
    void onError(int errorCode);
}

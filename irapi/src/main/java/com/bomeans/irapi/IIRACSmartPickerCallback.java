package com.bomeans.irapi;

/**
 * Created by admin on 2017/7/13.
 */
public interface IIRACSmartPickerCallback {

    void onPickerInfo(int currentIndex, int totalCount, String currentRemoteId);

    void onRemoteMatched(String remoteId);

    void onRemoteMatchFailed();
}

package com.bomeans.irapi;

import com.bomeans.IRKit.BIRACPicker;
import com.bomeans.IRKit.BIRACSmartPickerCallback;
import com.bomeans.IRKit.ConstValue;
import com.bomeans.IRKit.RemoteUID;

public class ACSmartPicker implements IACSmartPicker{

    private BIRACPicker mPicker;
    private String mCurrentKey = null;
    private Boolean mCompleted = false;
    private SmartPickerResult[] mResults = null;


    ACSmartPicker(BIRACPicker acSmartPicker){
        mPicker = acSmartPicker;
        reset();
    }

    @Override
    public String getPickerKey() {
        if (null == mPicker){
            return null;
        }

        if (mCompleted) {
            return null;
        }

        if (null == mCurrentKey){
            mCurrentKey = mPicker.begin();
        } else {
            mCurrentKey = mPicker.getNextKey();
        }

        return mCurrentKey;
    }

    @Override
    public int transmitIR() {
        if(null != mPicker){
            return mPicker.transmitIR();
        }
        return ConstValue.BIRTransmitFail;
    }

    @Override
    public int setPickerResult(Boolean isWorking) {

        if (null == mPicker) {
            mCompleted = true;
            mResults = null;
            return ConstValue.BIR_PFail;
        }

        int result = mPicker.keyResult(isWorking);
        RemoteUID[] remoteUidArray = null;

        switch (result){
            case ConstValue.BIR_PFind:
                mCompleted = true;
                remoteUidArray = mPicker.getPickerResult();
                break;

            case ConstValue.BIR_PFail:
                mCompleted = true;
                remoteUidArray = new RemoteUID[0];
                break;

            case ConstValue.BIR_PNext:
                mCompleted = false;
                mResults = null;
                break;

            case ConstValue.BIR_PUnknow:
            default:
                mCompleted = false;
                mResults = null;
                break;
        }

        if (mCompleted && (remoteUidArray != null)) {
            mResults = new SmartPickerResult[remoteUidArray.length];
            for (int i = 0; i < remoteUidArray.length; i++) {
                mResults[i] = new SmartPickerResult(remoteUidArray[i]);
            }
        }

        return result;
    }

    @Override
    public Boolean isPickerCompleted() {
        return mCompleted;
    }

    @Override
    public SmartPickerResult[] getPickerResult() {
        if (mCompleted) {
            return mResults;
        } else {
            return null;
        }
    }

    @Override
    public void reset() {
        mCurrentKey = null;
        mCompleted = false;
        mResults = null;
        if (null != mPicker) {
            mPicker.begin();
        }
    }

    @Override
    public void startAutoPicker(final IIRACSmartPickerCallback myCallback){

        mCompleted = false;
        mResults = null;

        if (null == mPicker) {
            mCompleted = true;
            mResults = null;

            if (null != myCallback) {
                myCallback.onRemoteMatchFailed();
            }

            return;
        }

        mPicker.beginAutoPicker(new BIRACSmartPickerCallback() {

            @Override
            public void onRemoteMatched(String remoteId) {

                // make this data ready before calling the callback.
                mCompleted = true;

                RemoteUID[] remoteUidArray = mPicker.getPickerResult();

                if (mCompleted && (remoteUidArray != null)) {
                    mResults = new SmartPickerResult[remoteUidArray.length];
                    for (int i = 0; i < remoteUidArray.length; i++) {
                        mResults[i] = new SmartPickerResult(remoteUidArray[i]);
                    }
                }

                if (null != myCallback) {
                    myCallback.onRemoteMatched(remoteId);
                }
            }

            @Override
            public void onRemoteMatchFailed() {
                mCompleted = true;
                mResults = null;

                if (null != myCallback) {
                    myCallback.onRemoteMatchFailed();
                }
            }

            @Override
            public void onPickerInfo(int currentIndex, int totalCount, String currentRemoteId) {
                if (null != myCallback) {
                    myCallback.onPickerInfo(currentIndex, totalCount, currentRemoteId);
                }
            }
        });
    }

    @Override
    public void setAutoPickerMatched(){
        if (null != mPicker) {
            mPicker.setAutoPickerMatched();
        }
    }

    @Override
    public void endAutoPicker() {
        if (null != mPicker) {
            mPicker.endAutoPicker();
        }
    }

    @Override
    public void setAutoPickerInterval(long msec) {
        if (null != mPicker) {
            mPicker.setAutoPickerInterval(msec);
        }
    }

    @Override
    public String getPickerInfo() {
        if (null == mPicker) {
            return "";
        }

        return mPicker.getPickerInfo();
    }

    @Override
    public boolean isPickerReady() {
        return ((null != mPicker) && mPicker.isPickerReady());
    }
}

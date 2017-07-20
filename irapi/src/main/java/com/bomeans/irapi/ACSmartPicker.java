package com.bomeans.irapi;


import com.bomeans.IRKit.ACSmartInfo;
import com.bomeans.IRKit.BIRACPicker;
import com.bomeans.IRKit.BIRACSmartPickerCallback;
import com.bomeans.IRKit.ConstValue;
import com.bomeans.IRKit.ModelItem;
import com.bomeans.IRKit.RemoteUID;

public class ACSmartPicker implements ITVSmartPicker{

    private com.bomeans.IRKit.ACSmartPicker biracPicker;
    ModelItem modelItem = null;
    private String mCurrentKey = null;
    RemoteUID mResults = null;
    private Boolean mCompleted = false;
    RemoteInfo model;
    int result;

    public ACSmartPicker(com.bomeans.IRKit.ACSmartPicker acSmartPicker){
        this.biracPicker = acSmartPicker;
    }

    @Override
    public void setNum(int num) {
            biracPicker.setTryKeyNum(num);
    }


    @Override
    public ACSmartInfo begin() {
        return biracPicker.begin();
    }

    public RemoteInfo getModel(){
        if(modelItem ==null){
           modelItem = begin().remote;
        }else {
            modelItem = biracPicker.getNextModel();
        }
        RemoteInfo modelInfo = new RemoteInfo(modelItem.model,modelItem.machineModel,modelItem.country,modelItem.releaseTime);
        return modelInfo;
    }
    public String getPickerKey() {
        if(null == biracPicker){
            return null;
        }

        if(null == mCurrentKey){
            ACSmartInfo picker = biracPicker.begin();
            mCurrentKey = picker.key;
        }else {
        mCurrentKey=  biracPicker.getNextKey();
        }
        return mCurrentKey;
    }

    public int transmitIR() {
        if(null != biracPicker){
            return biracPicker.transmitIR();
        }
        return ConstValue.BIRTransmitFail;
    }


    public SmartPickerResult[] getPickerResult() {
        SmartPickerResult[] smartPickerResult;
        if(mCompleted) {
            mResults = new RemoteUID(biracPicker.getPickerResult().typeID,
                    biracPicker.getPickerResult().brandID,
                    biracPicker.getPickerResult().modelID);
            smartPickerResult = new SmartPickerResult[]{new SmartPickerResult(mResults)};

        }else {
            smartPickerResult = null;
        }
        return smartPickerResult;
    }


    @Override
    public int setPickerResult(Boolean isWorking) {
        result = biracPicker.keyResult(isWorking);
        if(result==0){
            mCompleted = true;
        }
        return result;
    }
    @Override
    public void reset() {

    }
    @Override
    public Boolean isPickerCompleted() {
        return null;
    }

    public int getNowRemoteNum() {
        return biracPicker.getNowModelNum();
    }
    public int getRemoteCount() {
        return biracPicker.getModelCount();
    }

    public void startAutoPicker(final IIRACSmartPickerCallback autocallback){
        biracPicker.beginAutoPicker(new BIRACSmartPickerCallback() {
            @Override
            public void giveRemoteNum(int i, ModelItem modelItem) {
                autocallback.giveNowRemoteInfo(i,modelItem);
            }

            @Override
            public void NoMatches() {
                autocallback.Nomatch();
            }
        });
    }
    public void endPicker(){
        biracPicker.endAutoPicker();
    }
}

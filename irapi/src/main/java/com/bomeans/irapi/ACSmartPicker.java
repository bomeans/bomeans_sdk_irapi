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
    ModelItem nowmodelItem = null;
    private String mCurrentKey = null;
    RemoteUID mRemoteResults = null;
    private Boolean mCompleted = false;
    private Boolean mTransmitResponse = false;
    private Boolean mautopickerend=false;
    RemoteInfo model;
    int result;
    private SmartPickerResult[] mResults = null;


    public ACSmartPicker(com.bomeans.IRKit.ACSmartPicker acSmartPicker){
        this.biracPicker = acSmartPicker;
    }

    @Override
    public void setNum(int num) {
            biracPicker.setTryKeyNum(num);
    }


    @Override
    public ACSmartInfo begin() {
        modelItem = null;
        mCurrentKey= null;
        return biracPicker.begin();
    }

    public RemoteInfo getModel(){
        if(modelItem ==null){
           modelItem = begin().remote;
        }else {
            if(!mTransmitResponse) {
                modelItem = biracPicker.getNextModel();
            }
            if(mautopickerend){
                modelItem =nowmodelItem;
            mautopickerend = false;
            }
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
        if(mCompleted) {
            return mResults;
        }else {
            return null;
        }
    }


    @Override
    public int setPickerResult(Boolean isWorking) {
        mTransmitResponse= isWorking;
        result = biracPicker.keyResult(isWorking);
        switch (result){
            case ConstValue.BIR_PFind:
                mCompleted = true;
                mRemoteResults = new RemoteUID(biracPicker.getPickerResult().typeID,
                        biracPicker.getPickerResult().brandID,
                        biracPicker.getPickerResult().modelID);
                mResults = new SmartPickerResult[]{new SmartPickerResult(mRemoteResults)};
                break;
            case ConstValue.BIR_PFail:
                mCompleted = true;
                mResults = new SmartPickerResult[0];
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
        return result;
    }
    @Override
    public void reset() {
        begin();
    }
    @Override
    public Boolean isPickerCompleted() {
        return mCompleted;
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
                nowmodelItem = modelItem;
                mautopickerend = true;
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

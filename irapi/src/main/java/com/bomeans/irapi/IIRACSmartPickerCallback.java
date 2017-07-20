package com.bomeans.irapi;

import com.bomeans.IRKit.ModelItem;

/**
 * Created by admin on 2017/7/13.
 */
public interface IIRACSmartPickerCallback {
    void giveNowRemoteInfo(int var1, ModelItem var2);

    void Nomatch();
}

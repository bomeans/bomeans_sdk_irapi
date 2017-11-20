package com.bomeans.irapi;

/**
 * Created by ray on 2017/11/14.
 */

public interface IACSmartPicker extends ITVSmartPicker {

    /**
     * start a auto picker session. auto-picker: transmit IR signal one by one until the user
     * issues "setAutpPickerMatched()" as a valid IR signal is found (the appliance reacts to the
     * transmitted IR signal) or end of the possible signals (so no match is found)
     *
     * @param callback callback function to receive the status/result of the auto-picker.
     */
    void startAutoPicker(IIRACSmartPickerCallback callback);

    /**
     * whenever you want to end a auto-picker session, call this to terminate the timer scheduling
     * (for transmitting IR signals one by one)
     *
     * This aborts the auto-picker without getting the result.
     */
    void endAutoPicker();

    /**
     * once the target appliance responses to the transmitted IR signal, call this function to
     * stop the auto-picker and to get the result. The result will be passed back via callback.
     *
     * after invoking this function, you can also calling getPickerResult() to get the more
     * info about the matched remote.
     */
    void setAutoPickerMatched();

    /**
     * set the time interval among the IR signal transmission of the remotes to be matched.
     * the default value is 3000 (3-sec).
     * @param msec time interval among IR signal transmission. (available: 1000(1s) to 10000(10s))
     */
    void setAutoPickerInterval(long msec);
}

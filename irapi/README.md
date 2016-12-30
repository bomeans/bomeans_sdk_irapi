#Introduction

The IRAPI APIs are a wrapper of the IRKit APIs in the official release of IR SDK. It acts as a replacement of the IRKit APIs, aiming to provide more friendly interfaces for the user yet not altering the originally IRKit APIs.

#How to Use
##Adding SDK Files
IRAPI does not run alone, it still need the work with the original SDK side by side.
Therefore, you need to have the original SDK files copied to your project.

If you are using Eclipse + ADT:<br>
<li>Copy the sub-folders containing the .so files to the \libs folder in your project.</li>
<li>Copy the .jar files to the \libs folder in your project.</li>
<p>

If you are using Android Studio:<br>
<li>Copy the sub-folders containing the .so files to the \src\main\jniLibs folder in your project root.</li>
<li>Copy the .jar files to the \libs folder in your project root.</li>
Note: You also need to have the following settings in your build.gradle(Module:your_app):<br>
```
dependencies {
    ...
    compile fileTree(dir: 'libs', include: ['*.jar'])
    ...
}
```

##Adding irapi.jar
Simply copy the irapi.jar to your \libs folder in your project root.

##Adding App Permissions
The following permissions need to be added to your AndroidMenifest.xml:
```
android.permission.INTERNET
android.permission.ACCESS_WIFI_STATE
```
#Initial Setup
##Getting the API Key
You need to apply a valid API Key for the SDK to run normally.<br>
Apply the key here: http://www.bomeans.com/Mainpage/Apply/apikey

##Setting your SDK
```
IRAPI.init(apiKey, applicationContext)
```
where apiKey is the API Key issued by Bomeans Design. applicationContext is the application context of your app.

##Switch your target server if your target users are in China
If your target users locate in China, you can optionally switch the connected cloud server the one located in China for better performance.<br>
To switch to China server,
```
IRAPI.switchToChineseServer(true)
```
Note: If the above function is not call, a default server located outside China will be used.

#Getting the Basic Information/Lists
The SDK provides the following APIs for accessing the basic information of the remote controllers.<br>
These APIs are asynchronous, the results will be returned in the callbacks.

| API	| Description | Returned Object in Callback
| ------------- | ------------------ | -------------
| ```IRAPI.getTypeList``` | Get type list | List<TypeInfo>
| ```IRAPI.getBrandList``` | Get brand list | List<BrandInfo>
| ```IRAPI.getRemoteList```	| Get remote ID list |	List<RemoteInfo> (Note1)
| ```IRAPI.searchRemote```	| Get remote ID list by keyword |	SearchResult (Note2)

Notes

<li>1: Remote ID is the internal unique id for each remote controller in the database. This ID is not necessary the model name of the remote controller itself or the model name of the appliance.</li>
<li>2: The keywords for searching the remote controllers are usually the one containing the type, brand, and/or the button name. For example, "Turn on the Panasonic TV" is a valid search string that conatins the type (TV), brand name (Panasonic), and the optional button name (Turn on as power button.)</li>

#Creating the Remote Controller Instance

Two types of remote controllers can be created:

<li>Universal Remote Controller - Univeral remote Controller is a pseudo remote controller which contains several most used remote controllers. When a button of the universal remote is pressed, the IR signals corresponding the underlying remote controllers will be send in a batch.</li>
<li>Single Remote Controller - Single remote controller in the database.</li>

| API | Description | Callback | Remark
|------------------- | ---------------------------------------------------- | ----------- | ------------------------------
| ```IRAPI.createRemote``` | Create a single remote controller (with specific remote id) (Note1) | IRRemote | Both TV-type and AC-type remotes are supported.
| ```IRAPI.createSimplifiedUniversalRemote``` | Create a simplified-keys universal remote (Note2) | IRRemote | Both AC/TV-type remotes are supported.
| ```IRAPI.createFullUniversalRemote``` | Create a full-keys universal remote (Note3) | IRRemote | Only TV-type remotes are supported.

Note:

<li>1. Single remote controller: A remote controller that have a one-to-one key mapping of a specific real-world remote controller. The other kind of remote controller is the Universal Remote Controller (URC) which is a pesudo remote controller containing several most popular remotes of a specific brand in it. When a key button of a URC is pressed, a sequence of IR signals corresponding to each of the underlying remotes will be sent.</li>
<li>2. Simplified-keys URC: A universal remote which have only a limited key buttons (usually the most common keys) exposed for controlling.</li>
<li>3. Full-keys URC: A universal remote which have all the available key buttons exposed for controlling.</li>

#Remote Controller Manipulation

Once the Remote Controller instance (```IRRemote```) is created, you can manipulate the remote by using the following APIs:

| API | Description | TV-type<br>Remote | TV-type<br>Universal Remote | AC-type<br>Remote | AC-type<br>Universal Remote
| ------------- | ---------------------------------------------------------------| ----- | ----- | ----- | -----
| ```getBrandId``` | Get the brand id of this remote | V | V | V | V
| ```getModels``` | Get the appliance model names for this remote | V	| | V
| ```getRemoteId``` | Get the remote id of this remote | V | V | V | V 
| ```getKeyList``` | Get all key(button) of the remote | V | V | V | V
| ```transmitIR``` | Send the IR data (Note1) | V | V | V | V
| ```startTransmitRepeatedIR``` | Start a IR transmission<br>(Call endTrasmitIR to stop transmission) | V			
| ```endTransmitRepeatedIR``` | Stop IR transmission | V			
| ```setRepeatCount``` | Set the number of frames to be repeatly sent for a single press on a TV-type remote controller. | V | | | 
| ```acGetGuiFeatures``` | Get the GUI options for AC remote (Note2) | | | V |	
| ```acGetActiveKeys``` | Get the currently active key(button) of the AC remote | | | V | V
| ```acGetKeyOption``` | Get the currently active options of the specific key(button) of a AC remote  | | | V | V	
| ```acSetKeyOption``` | Set the currently active option of the specific key(button) of a AC remote<br>(Since v.20161228) | | | V | V
| ```acGetTimerKeys``` | Get the timer-related keys(buttons) of the AC remote | | | V |	
| ```acSetOnTime``` | Set the ON timer of the AC remote  | | | V |
| ```acSetOffTime``` | Set the OFF timer of the AC remote | | | V |
| ```acGetStateData``` | Get the data of current states of the AC remote<br>(For storing the states of the AC remote) | | | V | V	
| ```acSetStateData``` | Restore the current states of the AC remote | | | V | V	
    
Notes:

<li>1. For the AC remotes, an optional "key option" parameter can be passed to switch the remote controller to the specified key state (such as switch the mode to Cool mode directly.) If the "key option" is obmitted, the key state will switch to next available state cycliclly.</li>
<li>2. The GUI options (```ACGUIFeatures```) are for the GUI outlook of an AC-type remote controller.

| Category | Options | Description
| -------------- | ---------------- | --------------------------------------
| ```DisplayMode``` | ```NoDisplay``` | This AC remote does not have a display panel 
| | ```ValidWhilePoweredOn``` | This AC remote has a normal display panel <br>(The panel is off when power on; is off when power off)
| | ```AlwaysOn``` | The panel is always on regardless of the power state (on or off)
| ```TimerDisplayMode``` | ```None``` | No timer functions
| | ```Clock``` | Timer works in clock type
| | ```CountDown``` | Timer works in count-down type
| ```TimerOperationMode``` | ```modeUnknown``` |
| | ```mode1``` | 
| | ```mode2``` |
| | ```mode3``` |
| | ```mode4``` |
| | ```mode5``` |

#Smart Picker
Most used way to pick up a remote controller from the database containing massive remotes is the so-called smart picker. The user aim the remote controller to the appliance, press a test key to see if the appliance reactives to the key, and repeat this procedure until a proper remote is selected. 

The SDK provides some APIs for helping the Apps to integrate the above steps for TV-type remotes. Note for the AC-type remotes, you need to download the list of all AC remotes of the specified brand and show to the user one by one to test if the correctly remote is selected.

To create a smart picker instance, call ```IRAPI.createSmartPicker``` and get the returned instance of BIRTVPicker in the callback function. After that, you can manipulate the picker by the provided functions of the ```ITVSmartPicker``` interface.

| API | Description | Remark
| ------------- | --------------------------------------- | -----------------------------------
| ```reset``` | Re-start the smart picker | Call this method if need to re-start the picking process by reseting the picker's internal state.
| ```getPickerKey```	| Get the current picker key(button) for user to test | The key ID for testing is returned
| ```isPickerCompleted``` | Check if the picking process has completed. | 
| ```transmitIR``` | Transmit the IR data of the current testing key |
| ```setPickerResult```	| Pass the user feedback to the picker | 
| ```getPickerResult``` | Get the matched remotes once the test is completed | Return the matched remote ID(s) or empty list if failed.

#IR Learning 
##Introduction of IR Learning
Two operation modes are supported:
<li>Learn and Store: Act as a IR signal recorder/player. The IR signal is learned (recorded) and can be re-transmit (replay). The App or the host CPU is responsible for storing the learned IR data.</li>
<li>Learn and Match: The learned IR data is analyzed by the SDK for extracting the characteristics, these characteristics are then sent to the cloud for matching the existing remote controller(s).</li>

The "Learn and Store" is for traditional IR learning application. For the remote controller not registered to the cloud database, learning provides a way to copy the IR signal for controlling the target appliance.

For the "Learn and Match", it provides an alternative way to picking out the correct remote controller from the database. User can press only several keys of the remote controller and the cloud can find the same or similar remote controller for him.

###Learn and Store
The learned IR data (for a specific key on the remote) can be passed back to the App in a compressed form. The App or host application can save the data in its own storage with the specific key name or key ID. To replay the IR signal, simply read the data back from storage and send to the SDK for transmission.

![Fig.1](../_docs/learning_diagrams_1.png)

###Learn and Match
Learn and Match allow the learned IR data to be analyzed and sent to the cloud for matching the existing remote controllers in database. This is sutable for 
<li>Providing a easy way to find the exact the same or similar remote controller(s) by only a few key presses on the remote controller.</li>
<li>Download the similar remote controller to reduce the number of amount of keys to be learned. User need to learn the keys not exist or not matched with their target appliance.</li>

![Fig.2](../_docs/learning_diagrams_2.png)

##Learning APIs
The learning APIs are separated into two parts. The upper APIs are for App to issue commands to switch the IR Blaster into learning mode, and receive the learned IR data from the callbacks. The lower APIs are for passing the data comes from the IR blaster back to the SDK for processing. 

![Fig.3](../_docs/learning_diagrams_3.png)

###Lower APIs
SDK provides a BIRIRBlaster interface for briding the in/out data between the SDK and the IR Blaster hardware. The App should have a instance which extends the BIRIRBlaster interface, and passing this instance to the SDK via IRKit.setIRHW(). Once this is done, all communication data for the IR Blaster hardware will be passed through the instance the App provided. 

Note: How to handle the communication data in/out to/from the IR Blaster is depending on the product specific communication channel therefore is not covered in this document.

Here's an example:

```
public class MyNetworkIRDevice implements IIRBlaster {
    private IDataReceiveCallback mDataReceiveCallback;

    @Override
    public Boolean isConnected() {
        // just return true if the underlying hardware is connected.
        return true;
    }

    @Override
    public int transmitData(byte[] data) {

        // here is where you get the IR data.
        // This data bytes need to be relayed to the Bomeans MCU vai
        // physical connection (such as UART or I2C) or wireless connection
        // (such as WiFi, BLE, or Zigbee passthrough.
        String info = String.format("Transmit %d bytes:", data.length);
        for (int i = 0; i < data.length; i++) {
            info += String.format("%02X,", data[i]);
        }
        Log.d(DBG_TAG, info);

        return ConstValue.BIROK;
    }

    @Override
    public void setReceiveDataCallback(IDataReceiveCallback callback) {
        // You need to keep this callback function and call the
        // callback.onDataReceive(byte[] receivedData) with the
        // received data bytes as the parameter.
        mDataReceiveCallback = callback;
    }

    public void onDataArrived(byte[] receivedData) {

        // you might need to check the data integrity.

        if (null != mDataReceiveCallback) {
            mDataReceiveCallback.onDataReceived(receivedData);

            String info = String.format("Received %d bytes:", receivedData.length);
            for (int i = 0; i < receivedData.length; i++) {
                info += String.format("%02X,", receivedData[i]);
            }
            Log.d(DBG_TAG, info);
        }
    }
}
```

##Upper APIs
The App should first create a BIRReader instance for manipulating the learning functions.

Here's an example for creating a BIRReader instance:

```
// initialize the SDK
IRAPI.init(BOMEANS_SDK_API_KEY, getApplicationContext());

// create and get the IIRReader instance from the callback
IRAPI.createIRReader(getNew(), new IIRReaderCallback() {
    @Override
    public void onReaderCreated(IIRReader irReader) {
        // get the BIRReader instance
        mIrReader = irReader;
    }

    @Override
    public void onReaderCreateFailed() {
        // error handling
        Log.d(DBG_TAG, "ERROR]:failed to create ir reader!");
    }
});

```

Once the IIRReader instance is created, use the following IIRReader APIs to do the learning manipulations.

| API | Description
| ---------------- | ---------------------------------
| ```startLearningAndGetData``` | Start a learning session, and get the result from the callback
| ```startLearningAndSearchCloud``` | Start a learning session, and get the matched remote IDs from the callback
| ```stopLearning``` | Abort the learning (Note: Each learning session will be ended automatically either a valid IR signal is learned or 15 seconds time-out time is reached.)
| ```sendLearningData``` | The learning data got from ```startLearningAndGetData``` can be passed into this function for re-transmission.
| ```reset``` | Reset the ```startLearningAndSearchCloud``` session.(```startLearningAndSearchCloud``` will cache the previously passed-in learning data. If reset() is not called, invoke ```startLearningAndSearchCloud``` twice with different learning data will result in passing two learning data to the cloud to match the remote(s) conaining both IR data.)
| ```getBestMatches``` | Get the best match results
| ```getPossibleMatches``` | Get the other possible match results not included in the ```getBestMatches``` result
| ```getAllMatches``` | Get all matches, including those from ```getBestMatches``` and ```getPossibleMatches```
| ```getWaveCount``` | Get the number of waveform count of the learned IR signal. A high or low signal is counted as 1 waveform. (For debugging)
| ```getFrequency``` | Get the carrier frequency of the learned IR signal. (For debugging)
Example:

```
// IR Learning example

mIrReader.startLearningAndGetData(IIRReader.PREFER_REMOTE_TYPE.Auto, new IIRReader.IIRReaderFormatMatchCallback() {
    @Override
    public void onFormatMatchSucceeded(final IIRReader.ReaderMatchResult formatMatchResult) {
        // if you are not interested in parsing the learned data into IR format, skip this callback.
        // this callback is used mostly for debugging
        LearnAndSendActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (formatMatchResult.isAc()) {
                    // if the matched remote is AC-type 
                } else {
                    // if the matched remote is TV-type
                }
            }
        });
    }

    @Override
    public void onFormatMatchFailed(final IIRReader.FormatParsingErrorCode errorCode) {
        // if you are not interested in parsing the learned data into IR format, skip this callback.
        // this callback is used mostly for debugging
        LearnAndSendActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (errorCode) {
                    case LearningModeFailed:
                        // Learning mode failed
                        break;
                    case UnrecognizedFormat:
                        //Un-recognized Format
                        break;
                    case NoValidLearningData:
                    default:
                        //No Valid Learning Data
                        break;
                }
            }
        });
    }

    @Override
    public void onLearningDataReceived(byte[] learningData) {
    
        // you can remember the learned data for re-play later
        mLearnedDataForSending = learningData;

        // you can read the wave count (number of High/Low siignals) and the carrier frequency 
        // of the learned data. (Needed only for debugging)
        int waveCount = mIrReader.getWaveCount();
        int frequency = mIrReader.getFrequency();

        LearnAndSendActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // handle GUI if any
            }
        });
    }

    @Override
    public void onLearningDataFailed(final IIRReader.LearningErrorCode errorCode) {
    
        // GUI error handling
        LearnAndSendActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (errorCode) {
                    case LearningModeFailed:
                        //Learning Mode Failed
                        break;
                    case TimeOut:
                        //Time Out
                        break;
                    case IncorrectLearnedData:
                    default:
                        //Incorrect Data
                        break;
                }
            }
        });
    }
});

// Transmit Learned Data example:

mSendButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (mLearnedDataForSending != null) {
            if (null != mIrReader) {
                mIrReader.sendLearningData(mLearnedDataForSending);
            }
        }
    }
});


// Learn and Recognize example:

mIrReader.startLearningAndSearchCloud(false, preferRemoteType, new IIRReader.IIRReaderRemoteMatchCallback() {
    @Override
    public void onRemoteMatchSucceeded(final List<IIRReader.RemoteMatchResult> list) {
        // format match is usually for debugging, you can safely ignore this callback
        LearnAndRecognizeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // showing the matched format(s);
            }
        });
    }

    @Override
    public void onRemoteMatchFailed(IIRReader.CloudMatchErrorCode errorCode) {
        // format match is usually for debugging, you can safely ignore this callback
        LearnAndRecognizeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // error handling
            }
        });
    }

    @Override
    public void onFormatMatchSucceeded(final List<IIRReader.ReaderMatchResult> list) {
        // This is where you get the matched remote info list
        
        LearnAndRecognizeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // showing the matched remote(s)
            }
        });
    }

    @Override
    public void onFormatMatchFailed(final IIRReader.FormatParsingErrorCode errorCode) {
        // error handling

        LearnAndRecognizeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorCode == IIRReader.FormatParsingErrorCode.UnrecognizedFormat) {
                    // handling Un-recognized Format
                } else {
                    // handling Learning Failed
                }
            }
        });
    }
});

```







#bomeans_sdk_irapi

##Download
[download irapi.jar (current version)](https://github.com/bomeans/bomeans_sdk_irapi/tree/master/irapi/release)

Note: The irapi.jar does not work alone. You still need to download the [Bomeans SDK](https://github.com/bomeans/bomeans_sdk_bin/tree/master/Android).

##Documentation
[IRAPI documentation](https://github.com/bomeans/bomeans_sdk_irapi/tree/master/irapi)

##Introduction
IRAPI is a wrapper for original IRKit APIs of Bomeans IR SDK(Android).
IRAPI APIs include all the functionality of IRKit APIs. We recommend you to use IRAPI for new projects.

The Bomeans IR SDK (Android) supports native IRKit APIs. To improve the usability yet still maintain the compatibility, the new IRAPI APIs are created as a wrapper for the original IRKit APIs. The IRKit APIs are kept without being changed.

##How to Use
To use the IRAPI APIs, simply add the irapi.jar, alone with the irkit.jar and irnaticecore.jar, to the libs path of your Android project. The native .so library files should also be placed under the jniLibs folder. 

Though you can call both IRKit and IRAPI APIs in the same application, we suggest you choose only one set of APIs to avoid complexity.

##Test App Screen Shot:
[Note] You need to apply a SDK key for the demo application to run. Contact Bomeans Design for the key.

![App Screenshot](_docs/Screenshot_01.jpg?raw=true)

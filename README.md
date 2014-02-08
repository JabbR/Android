## JabbR for Android 
JabbR for Android is a chat application that uses SignalR for real-time communication. 

### Features and Bugs
If you want to discuss the features join discussion in the [meta](https://jabbr.net/#/rooms/meta) room on jabbr. 

### Building the sources

To build the project, it required the following files to be present in the libs directory:
	- signalr-client-sdk.jar (SignalR Library for Java)
	- signalr-client-sdk-android.jar (SignalR Platform tools for Android)
	- gson-2.2.2.jar (Json Library)

This project uses the ActionBar feature available in the android-support-v7-appcompat project provided with the Android SDK. To build, it is required to add a reference of the appcompat project in this project. 
For more instructions to install the appcompat project go to:
http://developer.android.com/tools/support-library/setup.html#libs-with-res


To get the Gson Library:
	- Run getLibs.ps1 or getLibs.sh in the libs directory to download gson-2.2.2.jar


To get the SignalR Library for Java and SignalR Platform tools for Android, download the sources and build the libraries from: https://github.com/SignalR/java-client

#### Getting Involved
We welcome contributions from experienced developers.  You can get involved by logging bugs in github, hacking on the source, or discussing issues / features in the [meta](https://jabbr.net/#/rooms/meta) room.

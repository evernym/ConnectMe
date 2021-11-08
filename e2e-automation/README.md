# Pre requisite to run

- JDK 8
- Node >12.13 . Preferred way to install node is via [nvm](https://www.sitepoint.com/quick-tip-multiple-versions-node-nvm/)
- [React native setup](http://facebook.github.io/react-native/docs/getting-started.html). Use tab `Building Projects with Native Code`.
- [Appium](https://appium.io/) 1.15.1 or higher
- Android
    - Android SDK
    - Android Studio 3.1.4 or higher
    - Set environment variables:
        ```
        export JAVA_HOME=/path/to/java
        export ANDROID_HOME=/path/to/Android/sdk
        export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
        export PATH="/usr/local/bin:/usr/local/sbin:$PATH"
        ```
- iOS
    - XCode 11 +
    - [Real Device Setup](https://appium.io/docs/en/drivers/ios-xcuitest-real-devices/#configuring-the-app-under-test)

# Steps to run

> Note: Test currently properly working only on real devices. Emulators are not supported!

- Start Appium server. 
- Run [VAS Server](./appium-launcher/vas-server.py)
    - Start Ngrok on 1338 HTTP port
    - Copy URL and put into `VAS_Server_Link` setting in [config](./src/test/java/utility/Config.java)
    - Go to `e2e-automation/appium-launcher` folder
    - Start server with `python3 vas-server.py`
- Android:
  - Update [config](./src/test/java/utility/Config.java) file:
    - Put `android` into `Device_Type` setting.
    - Put your device id into `Device_Name` setting (use `adb devices` to get list of devices).
    - Make sure that Appium's chromedriver version matches Chrome's version on the device. If they are different you need to download a proper driver and add `chromedriverExecutable` capability (path to the driver on your OS) in the [file](./src/test/java/utility/BrowserDriver.java)
- iOS:
  - Update [config](./src/test/java/utility/Config.java) file:
      - Put `iOS` into `Device_Type` setting.
      - Put your device name into `Device_Name` setting.
      - Put your device id into `Device_UDID` setting.
- Run tests: `mvn test`

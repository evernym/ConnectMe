# Pre requisite to run

- JDK 8
- Node >12.13 . Preferred way to install node is via [nvm](https://www.sitepoint.com/quick-tip-multiple-versions-node-nvm/)
- [React native setup](http://facebook.github.io/react-native/docs/getting-started.html). Use tab `Building Projects with Native Code`.
- Appium 1.15.1 or higher
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

# Steps to run

- Create and Start Android emulator
- Start Appium server
- Run [VAS Server](./appium-launcher/vas-server.py)
    - Start Ngrok on 1338 HTTP port
    - Copy URL and put into `VAS_Server_Link` setting in [config](./src/test/java/utility/Config.java)
    - Go to `e2e-automation/appium-launcher` folder
    - Start server with `python3 vas-server.py`
- Make sure that Appium's chromedriver version matches Chrome's version on the device. You can specify path to your `chromedriverExecutable` in [file](./src/test/java/utility/BrowserDriver.java)
- Run tests: `mvn test`





package test.java.utility;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;
import test.java.utility.Config;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Singleton for native ConnectMe app
 */
public class AppDriver {

    private static AppiumDriver driver;
    public static final int EXTRA_LARGE_TIMEOUT = 120; // 60?
    public static final int LARGE_TIMEOUT = 45; // 60
    public static final int SMALL_TIMEOUT = 15;
    public static final int SUPER_SMALL_TIMEOUT = 10;

    private static DesiredCapabilities getSharedCapabilitiesForAndroid() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.NO_RESET, true);
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60000);
        caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "me.connect");
        caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".MainActivity");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, Config.Device_Name);
        return caps;
    }

    private static DesiredCapabilities getSharedCapabilitiesForIos() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.NO_RESET, true);
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60000);
        caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.evernym.connectme.callcenter");
        caps.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID, "ES8QU3D2A4");
        caps.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID, "iPhone Developer");
        return caps;
    }

    private static DesiredCapabilities getLocalCapabilitiesForIos() {
        DesiredCapabilities caps = getSharedCapabilitiesForIos();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14.5");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, Config.Device_Name);
        caps.setCapability(MobileCapabilityType.UDID, Config.Device_UDID);
        caps.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, true);
        caps.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true); // true
        return caps;
    }

    private static DesiredCapabilities getAwsCapabilitiesForIos() {
        DesiredCapabilities caps = getSharedCapabilitiesForIos();
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "13.0");
        caps.setCapability("waitForQuiescence", false);
        caps.setCapability("shouldUseSingletonTestManager", true);
        caps.setCapability(IOSMobileCapabilityType.WDA_STARTUP_RETRY_INTERVAL, 1000);
        return caps;
    }

    public static AppiumDriver getDriver() {
        if (driver == null) {
            String deviceType = Config.Device_Type;
            try {
                DesiredCapabilities capabilities;
                if (deviceType.equals("iOS") || deviceType.equals("iOSSimulator")) {
                    capabilities = getLocalCapabilitiesForIos();
                    driver = new IOSDriver(new URL(Config.Appium_Server), capabilities);
                    System.out.println("connectMe application launched successfully in iOS");
                } else if (deviceType.equals("awsiOS")) {
                    capabilities = getAwsCapabilitiesForIos();
                    driver = new IOSDriver(new URL(Config.Appium_Server), capabilities);
                    System.out.println("connectMe application launched successfully in iOS");
                } else if (deviceType.equals("android")) {
                    capabilities = getSharedCapabilitiesForAndroid();
                    driver = new AndroidDriver(new URL(test.java.utility.Config.Appium_Server), capabilities);
                    System.out.println("connectMe application launched successfully in android");
                } else if (deviceType.equals("awsAndroid")) {
                    capabilities = getSharedCapabilitiesForAndroid();
                    capabilities.setCapability(MobileCapabilityType.PRINT_PAGE_SOURCE_ON_FIND_FAILURE, true);
                    driver = new AndroidDriver(new URL(test.java.utility.Config.Appium_Server), capabilities);
                    System.out.println("connectMe application launched successfully in android");
                }
                driver.manage().timeouts().implicitlyWait(LARGE_TIMEOUT, TimeUnit.SECONDS);
            } catch (Exception e) {
                Reporter.log("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
                System.out.println("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
                Assert.fail();
            }
        }

        return driver;
    }

    /**
     * Instantiates new IosDriver with target .ipa bundle
     * @param bundlePath path to the .ipa, can be local or URL
     * @return new Appium driver casted from IosDriver to make usage close to seamless
     */
    public static AppiumDriver getIosDriverWithProvidedBundle(String bundlePath)
    {
        try {
            DesiredCapabilities caps;
            if(Config.Device_Type.equals("awsiOS")) {
                caps = getAwsCapabilitiesForIos();
            }
            else {
                caps = getLocalCapabilitiesForIos();
            }
            caps.setCapability(MobileCapabilityType.APP, bundlePath);
            driver = new IOSDriver(new URL(Config.Appium_Server), caps);
        }
        catch (Exception e) {
            Reporter.log("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
            System.out.println("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
            Assert.fail();
        }
        driver.manage().timeouts().implicitlyWait(LARGE_TIMEOUT, TimeUnit.SECONDS);
        return driver;
    }

    /**
     * Shuts down driver and removes singleton instance if available
     */
    public static void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

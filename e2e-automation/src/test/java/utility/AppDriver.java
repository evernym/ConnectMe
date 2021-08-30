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

public class AppDriver {

    private static AppiumDriver driver;
    public static final int EXTRA_LARGE_TIMEOUT = 120;
    public static final int LARGE_TIMEOUT = 60;
    public static final int SMALL_TIMEOUT = 15;
    public static final int SUPER_SMALL_TIMEOUT = 10;

    private static DesiredCapabilities getSharedCapabilitiesForAndroid() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.NO_RESET, true);
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60000");
        caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "me.connect");
        caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".MainActivity");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, Config.Device_Name);
        return caps;
    }

    private static DesiredCapabilities getSharedCapabilitiesForIos() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.NO_RESET, true);
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60000");
        caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.evernym.connectme.callcenter");
        caps.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID, "ES8QU3D2A4");
        caps.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID, "iPhone Developer");
        return caps;
    }

    public static AppiumDriver getDriver() {
        if (driver == null) {
            String deviceType = Config.Device_Type;
            try {
                DesiredCapabilities capabilities;
                if (deviceType.equals("iOS") || deviceType.equals("iOSSimulator")) {
                    capabilities = getSharedCapabilitiesForIos();
                    capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
                    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                    capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14.5");
                    capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Config.Device_Name);
                    capabilities.setCapability(MobileCapabilityType.UDID, Config.Device_UDID);
                    capabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, true);
                    capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
                    driver = new IOSDriver(new URL(Config.Appium_Server), capabilities);
                    System.out.println("connectMe application launched successfully in iOS");
                } else if (deviceType.equals("awsiOS")) {
                    capabilities = getSharedCapabilitiesForIos();
                    capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "13.0");
                    capabilities.setCapability("waitForQuiescence", false);
                    capabilities.setCapability("shouldUseSingletonTestManager", true);
                    capabilities.setCapability(IOSMobileCapabilityType.WDA_STARTUP_RETRY_INTERVAL, "1000");
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
            } catch (Exception e) {
                Reporter.log("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
                System.out.println("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
                Assert.fail();
            }
        }
        driver.manage().timeouts().implicitlyWait(LARGE_TIMEOUT, TimeUnit.SECONDS);

        return driver;
    }

    public static AppiumDriver getIosDriverWithProvidedBundle(String bundlePath)
    {
        try {
            DesiredCapabilities caps = getSharedCapabilitiesForIos();
            caps.setCapability(MobileCapabilityType.APP, bundlePath);
            driver = new AndroidDriver(new URL(test.java.utility.Config.Appium_Server), caps);
        }
        catch (Exception e) {
            Reporter.log("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
            System.out.println("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
            Assert.fail();
        }
        driver.manage().timeouts().implicitlyWait(LARGE_TIMEOUT, TimeUnit.SECONDS);
        return driver;
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}

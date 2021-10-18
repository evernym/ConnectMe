package test.java.utility;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;
import test.java.utility.AppDriver;
import test.java.utility.Config;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Singleton for mobile web browser usage
 */
public class BrowserDriver {

	private static AppiumDriver driver;

    private static DesiredCapabilities getSharedCapabilitiesForAndroid()
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("newCommandTimeout", 60000);
        capabilities.setCapability("deviceName", Config.Device_Name);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
        capabilities.setCapability("autoAcceptAlerts", true);

        return capabilities;
    }

	public static AppiumDriver getDriver() {
		if (driver == null) {
			String deviceType = Config.Device_Type;
            DesiredCapabilities capabilities;

			try {
				if (deviceType.equals("iOS") || deviceType.equals("iOSSimulator")) {
					driver = AppDriver.getDriver();
					System.out.println("safari browser launched successfully in iOS");
				} else if (deviceType.equals("awsiOS")) {
					driver = AppDriver.getDriver();
					System.out.println("safari browser launched successfully in iOS");
				} else if (deviceType.equals("android")) {
                    capabilities = getSharedCapabilitiesForAndroid();
					capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
					driver = new AndroidDriver(new URL(test.java.utility.Config.Appium_Server), capabilities);
                    System.out.println("chrome browser launched successfully in Android");
				} else if (deviceType.equals("awsAndroid")) {
					capabilities = getSharedCapabilitiesForAndroid();
					driver = new AndroidDriver(new URL(test.java.utility.Config.Appium_Server), capabilities);
					System.out.println("chrome browser launched successfully in Android");
				}
                driver.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
			} catch (Exception e) {
				Reporter.log("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
				System.out.println("Class Setup | Method OpenBrowser | Exception desc : " + e.getMessage());
				Assert.fail();
			}
		}
		return driver;
	}

	public static void closeApp() {
		if (driver != null) {
			if ((Config.Device_Type.equals("android") || Config.Device_Type.equals("awsAndroid"))) {
				driver.closeApp();
			}
		}
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

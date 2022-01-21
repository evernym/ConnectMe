package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;
import test.java.utility.Helpers;
import test.java.utility.AppDriver;

import java.util.HashMap;

public class InstallAppFromAppCenterTest extends IntSetup {

    private static final String APP_PKG = "me.connect";
    private static final String APP_ACT = ".MainActivity";
    private static final String BUNDLE_ID = "com.evernym.connectme.callcenter";

    @Test
    public void installRcFromAppCenter() throws Exception {
        driverApp.launchApp();

        // move all logic below without removing app
//        if(Helpers.getPlatformType().equals(Platform.ANDROID)) {
//            driverApp.removeApp("me.connect");
//        } else {
//            driverApp.removeApp("com.evernym.connectme.callcenter");
//        }

        try {
            String appPath = AppCenterAPI.getReleaseCandidateAppDownloadUrl(Config.Device_Type);
            System.out.println("APP PATH >>> " + appPath);

            if(Helpers.getPlatformType().equals(Platform.ANDROID)) {
                driverApp.installApp(appPath);
                // +
                Activity activity = new Activity(APP_PKG, APP_ACT);
                AndroidDriver driver = (AndroidDriver)driverApp;
                driver.startActivity(activity);
            }
            else
            {
//                // iOS-specific: cannot install app directly
//                driverApp = AppDriver.getIosDriverWithProvidedBundle(appPath);

                HashMap<String, String> bundleArgs = new HashMap<>();
                bundleArgs.put("bundleId", BUNDLE_ID);
                driverApp.executeScript("mobile: terminateApp", bundleArgs);

                HashMap<String, String> installArgs = new HashMap<>();
                installArgs.put("app", appPath);
                driverApp.executeScript("mobile: installApp", installArgs);

                driverApp.executeScript("mobile: launchApp", bundleArgs);
            }

            Thread.sleep(5000);
            driverApp.closeApp();
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}

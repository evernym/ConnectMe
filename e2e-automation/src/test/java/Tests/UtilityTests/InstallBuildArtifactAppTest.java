package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;
import test.java.utility.AppDriver;
import test.java.utility.Helpers;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class InstallBuildArtifactAppTest extends IntSetup {
    @Test
    public void installBuildArtifact() throws Exception {
       try {
            String appPath = AppCenterAPI.getLatestAppDownloadUrl(Config.Device_Type);
            System.out.println("APP PATH >>> " + appPath);

            if(Helpers.getPlatformType().equals(Platform.ANDROID)) {
                driverApp.launchApp();
                driverApp.installApp(appPath);
                // + official docs steps
                Activity activity = new Activity(APP_PKG, APP_ACT);
                AndroidDriver driver = (AndroidDriver)driverApp;
                driver.startActivity(activity);
            }
            else
            {
                // official docs steps
                driverApp.launchApp();

                HashMap<String, String> bundleArgs = new HashMap<>();
                bundleArgs.put("bundleId", BUNDLE_ID);
                driverApp.executeScript("mobile: terminateApp", bundleArgs);

                HashMap<String, String> installArgs = new HashMap<>();
                installArgs.put("app", appPath);
                driverApp.executeScript("mobile: installApp", installArgs);

                driverApp.executeScript("mobile: launchApp", bundleArgs);
            }
       }
       catch (Exception e)
       {
           System.out.println(Arrays.toString(e.getStackTrace()));
           Assert.fail(e.getMessage());
       }
    }
}

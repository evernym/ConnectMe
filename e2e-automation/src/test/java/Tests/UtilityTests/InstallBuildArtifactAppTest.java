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
//        try {
            String appPath = AppCenterAPI.getLatestAppDownloadUrl(Config.Device_Type);
            System.out.println("APP PATH >>> " + appPath);

            if(Helpers.getPlatformType().equals(Platform.ANDROID)) {
//                String artifactsDir = System.getenv("DEVICEFARM_LOG_DIR");
//                FileUtils.copyURLToFile(new URL(appPath), new File("cm.apk")); //download file
//                File appDir = new File(artifactsDir, "cm.apk"); //set file
//                driverApp.installApp(appDir.getAbsolutePath());
                // -----------------------------------------------
                driverApp.launchApp();
//                driverApp.terminateApp(APP_PKG);
                driverApp.installApp(appPath);
//                driverApp.closeApp();
                // +
                Activity activity = new Activity(APP_PKG, APP_ACT);
                AndroidDriver driver = (AndroidDriver)driverApp;
                driver.startActivity(activity);
            }
            else
            {
//                // iOS-specific: cannot install app directly
//                driverApp = AppDriver.getIosDriverWithProvidedBundle(appPath);

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
//        }
//        catch (Exception e)
//        {
//            System.out.println(Arrays.toString(e.getStackTrace()));
//            Assert.fail(e.getMessage());
//        }
    }
}

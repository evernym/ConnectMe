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

public class InstallBuildArtifactAppTest extends IntSetup {
    @Test
    public void installBuildArtifact() throws Exception {
        try {
            String appPath = AppCenterAPI.getLatestAppDownloadUrl(Config.Device_Type);
            System.out.println("APP PATH >>> " + appPath);

            if(Helpers.getPlatformType().equals(Platform.ANDROID)) {
//                String artifactsDir = System.getenv("DEVICEFARM_LOG_DIR");
//                FileUtils.copyURLToFile(new URL(appPath), new File("cm.apk")); //download file
//                File appDir = new File(artifactsDir, "cm.apk"); //set file
//                driverApp.installApp(appDir.getAbsolutePath());
                // -----------------------------------------------
                driverApp.installApp(appPath);
                // +
//                Activity activity = new Activity(APP_PKG, APP_ACT);
//                AndroidDriver driver = (AndroidDriver)driverApp;
//                driver.startActivity(activity);
            }
            else
            {
                // iOS-specific: cannot install app directly
                driverApp = AppDriver.getIosDriverWithProvidedBundle(appPath);
            }
        }
        catch (Exception e)
        {
            System.out.println(Arrays.toString(e.getStackTrace()));
            Assert.fail(e.getMessage());
        }
    }
}

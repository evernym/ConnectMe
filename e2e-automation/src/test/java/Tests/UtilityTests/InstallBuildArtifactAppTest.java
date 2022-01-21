package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;
import test.java.utility.AppDriver;
import test.java.utility.Helpers;

public class InstallBuildArtifactAppTest extends IntSetup {
    @Test
    public void installBuildArtifact() throws Exception {
        try {
            String appPath = AppCenterAPI.getLatestAppDownloadUrl(Config.Device_Type);
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
                // iOS-specific: cannot install app directly
                driverApp = AppDriver.getIosDriverWithProvidedBundle(appPath);
            }
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}

package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;
import test.java.utility.Helpers;
import test.java.utility.AppDriver;

public class InstallAppFromAppCenterTest extends IntSetup {

    @Test
    public void installRcFromAppCenter() throws Exception {
        driverApp.launchApp();
        if(Helpers.getPlatformType().equals(Platform.ANDROID)) {
            driverApp.removeApp("me.connect");
        } else {
            driverApp.removeApp("com.evernym.connectme.callcenter");
        }

        try {
            String appPath = AppCenterAPI.getReleaseCandidateAppDownloadUrl(Config.Device_Type);
            System.out.println("APP PATH >>> " + appPath);

            if(Helpers.getPlatformType().equals(Platform.ANDROID)) {
                driverApp.installApp(appPath);
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

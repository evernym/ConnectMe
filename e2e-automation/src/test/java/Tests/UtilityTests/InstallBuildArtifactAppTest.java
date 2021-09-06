package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
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

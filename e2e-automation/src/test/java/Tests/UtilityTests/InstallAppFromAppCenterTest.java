package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;


public class InstallAppFromAppCenterTest extends IntSetup {

    @Test
    public void installRcFromAppCenter() throws Exception {
        driverApp.launchApp();
        driverApp.removeApp("me.connect");

        try {
            String appPath = AppCenterAPI.getReleaseCandidateAppDownloadUrl(Config.Device_Type);
            driverApp.installApp(appPath);
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}

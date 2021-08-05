package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;


public class InstallBuildArtifactAppTest extends IntSetup {
    @Test
    public void installBuildArtifact() throws Exception {
        try {
            String appPath = AppCenterAPI.getLatestAppDownloadUrl(Config.Device_Type);
            driverApp.installApp(appPath);
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}

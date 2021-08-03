package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
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
            System.out.println(e.getMessage());
            System.exit(1); // don't run other tests if this fails
        }
    }
}

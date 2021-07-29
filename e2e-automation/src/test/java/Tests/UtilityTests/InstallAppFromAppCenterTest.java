package test.java.Tests.UtilityTests;

import appModules.AppCenterAPI;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;
import io.appium.java_client.AppiumDriver;

import static test.java.utility.IntSetup.driverApp;


public class InstallAppFromAppCenterTest extends IntSetup {

    @Test
    public void installRcFromAppCenter() throws Exception {
        driverApp.launchApp();
        driverApp.removeApp("me.connect");

        try {
            String appPath = AppCenterAPI.getAppDownloadUrl(Config.Device_Type);
            driverApp.installApp(appPath);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1); // don't run other tests if this fails
        }
    }
}

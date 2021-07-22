package Tests;

import appModules.AppCenterAPI;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;


public class InstallAppFromAppCenter extends IntSetup {

    @Test
    public void installRcFromAppCenter() throws Exception {
        String appPath = AppCenterAPI.downloadRelevantApp(Config.Device_Type);
        driverApp.installApp(appPath);
    }
}

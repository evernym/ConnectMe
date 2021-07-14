package Tests;

import appModules.AppCenterAPI;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;
import test.java.utility.Config;


public class InstallAppFromAppCenter extends IntSetup {

    @Test
    public void installRcFromAppCenter() throws Exception {
        String appPath = AppCenterAPI.downloadRelevantApp(Config.Device_Type);
        driverApp.installApp(appPath);
    }

}

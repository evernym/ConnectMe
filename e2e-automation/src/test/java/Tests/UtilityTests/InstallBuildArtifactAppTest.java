package test.java.Tests.UtilityTests;

import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;

import java.util.Locale;

import static test.java.utility.IntSetup.driverApp;


public class InstallBuildArtifactAppTest extends IntSetup {
    public static String apkPath = "android/app/build/outputs/apk/release/app-arm64-v8a-release.apk";
    public static String ipaPath = "ios/ConnectMe.ipa";

    @Test
    public void installBuildArtifact() throws Exception {
        try {
            if (Config.Device_Type.toLowerCase(Locale.ROOT).contains("android")) {
                driverApp.installApp(apkPath);
            } else if (Config.Device_Type.toLowerCase(Locale.ROOT).contains("ios")) {
                driverApp.installApp(ipaPath);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1); // don't run other tests if this fails
        }
    }
}

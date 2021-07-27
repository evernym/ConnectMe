package test.java.Tests.UtilityTests;

import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;

import java.util.Locale;


public class InstallBuildArtifactAppTest extends IntSetup {
    public static String apkPath = "android/app/build/outputs/apk/release/app-arm64-v8a-release.apk";
    public static String ipaPath = "ios/ConnectMe.ipa";

    @Test
    public void installBuildArtifact() throws Exception {
      if(Config.Device_Type.toLowerCase(Locale.ROOT).contains("android")) {
        driverApp.installApp(apkPath);
      }
      else if (Config.Device_Type.toLowerCase(Locale.ROOT).contains("ios")) {
        driverApp.installApp(ipaPath);
      }
    }

}

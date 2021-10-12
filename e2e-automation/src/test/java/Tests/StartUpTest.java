package test.java.Tests;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObjects.PassCodePageNew;
import pageObjects.StartUpPageNew;
import test.java.utility.IntSetup;
import test.java.utility.AppDriver;

public class StartUpTest extends IntSetup {

    @BeforeClass
    public void BeforeClassSetup() {
        driverApp.launchApp();
    }

    @Test
    public void setUpWizardTest() throws Exception {
        System.out.println(">>>>>>>>>>>>>>>> Device info");
        try {
            System.out.println("Device name: " + driverApp.getCapabilities().getCapability("deviceName").toString());
        }
        catch (Exception e) {}
        try {
            System.out.println("Device model: " + driverApp.getCapabilities().getCapability("deviceModel").toString());
        }
        catch (Exception e) {}
        try {
            System.out.println("Cap type:version: " + driverApp.getCapabilities().getCapability("CapabilityType.VERSION").toString());
        }
        catch (Exception e) {}
        try {
            System.out.println("Shell model: " + driverApp.executeScript("mobile: shell", ImmutableMap.of("command", "getprop ro.product.model")).toString());
        }
        catch (Exception e) {}

        try {
            System.out.println("Shell product name: " + driverApp.executeScript("mobile: shell", ImmutableMap.of("command", "getprop ro.product.name")).toString());
        }
        catch (Exception e) {}

        try {
            System.out.println("Shell product device: " + driverApp.executeScript("mobile: shell", ImmutableMap.of("command", "getprop ro.product.device")).toString());
        }
        catch (Exception e) {}
            System.exit(1);

        try {
            startUpPageNew.setUpButton.click();
            for (int i = 0; i < 2; i++) {
              passCodePageNew.enterPassCode();
            }
            startUpPageNew.switchEnv();
        } catch (Exception e) {
            System.exit(1); // don't run other tests if this fails
        }
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }

}

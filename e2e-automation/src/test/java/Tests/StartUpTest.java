package test.java.Tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;

public class StartUpTest extends IntSetup {

    @BeforeClass
    public void BeforeClassSetup() {
        System.out.println("Start Up Test Suite has been started!");
//        reloadDriversAndPos(); // for upgrade path test
        driverApp.launchApp();
    }

    @Test
    public void setUpWizardTest() throws Exception {
        System.out.print("Contexts >>> " + driverApp.getContextHandles());
        try {
            startUpPageNew.setUpButton.click();
            for (int i = 0; i < 2; i++) {
              passCodePageNew.enterPassCode();
            }
            startUpPageNew.switchEnv();
            homePageNew.homeHeader.isDisplayed();
        } catch (Exception e) {
            System.exit(1); // don't run other tests if this fails
        }
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
        System.out.println("Start Up Test Suite has been finished!");
    }

}

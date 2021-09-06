package test.java.Tests;

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
        reloadDriversAndPos();
    }

    @Test
    public void setUpWizardTest() throws Exception {
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

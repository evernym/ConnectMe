package test.java.Tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;


public class StartUpTest extends IntSetup {

    @BeforeClass
    public void BeforeClassSetup() {
        driverApp.launchApp();
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

    @Test(dependsOnMethods = "setUpWizardTest")
    public void checkMenuElementsVisibility() throws Exception {
      homePageNew.homeHeader.isDisplayed();
      homePageNew.tapOnBurgerMenu();
      homePageNew.scanButton.isDisplayed();
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }

}

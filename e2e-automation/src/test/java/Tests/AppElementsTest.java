package test.java.Tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.utility.Config;
import test.java.utility.IntSetup;


public class AppElementsTest extends IntSetup {

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
        passCodePageNew.openApp();
    }

    @Test
    public void checkHome() throws Exception {
        // Home
        homePageNew.homeHeader.isDisplayed();
        homePageNew.burgerMenuButton.isDisplayed();
        homePageNew.scanButton.isDisplayed();
    }

    @Test(dependsOnMethods = "checkHome")
    public void checkMenu() throws Exception {
        homePageNew.burgerMenuButton.click();
        menuPageNew.menuContainer.isDisplayed();
        menuPageNew.banner.isDisplayed();
        menuPageNew.logo.isDisplayed();
        menuPageNew.builtByFooter.isDisplayed();
        menuPageNew.versionFooter.isDisplayed();

        // Avatar
        menuPageNew.userAvatar.click();
        try {
            menuPageNew.okButton.click();
//            try {
                menuPageNew.menuAllowButton.click();
//            } catch (Exception ex) {
//                menuPageNew.menuAllowButtonAlt.click();
//            }
        }
        catch (NoSuchElementException e) {
            System.out.println("Permissions already have been granted!");
        }
        finally {
            Thread.sleep(1000);
            if((Config.Device_Type.equals("android")||Config.Device_Type.equals("awsAndroid"))) {
                ((AndroidDriver) driverApp).pressKey(new KeyEvent(AndroidKey.BACK));
            } else {
                menuPageNew.cancelButton.click();
            }
        }
        menuPageNew.homeButton.click();

        // My Connections
        homePageNew.burgerMenuButton.click();
        menuPageNew.myConnectionsButton.click();
        myConnectionsPageNew.myConnectionsHeader.isDisplayed();
        homePageNew.scanButton.isDisplayed();

        // My Credentials
        homePageNew.burgerMenuButton.click();
        menuPageNew.myCredentialsButton.click();
        myCredentialsPageNew.myCredentialsHeader.isDisplayed();
        homePageNew.scanButton.isDisplayed();

        // Settings
        homePageNew.burgerMenuButton.click();
        menuPageNew.settingsButton.click();
        // TODO check Settings header

        // Go Back Home
        homePageNew.burgerMenuButton.click();
        menuPageNew.homeButton.click();
        homePageNew.homeHeader.isDisplayed();
    }

    @Test(dependsOnMethods = "checkMenu")
    public void checkQrScanner() throws Exception {
        homePageNew.scanButton.isDisplayed();
        homePageNew.scanButton.click();
        try {
            qrScannerPageNew.scannerAllowButton.click();
        }
        catch (NoSuchElementException e) {
            System.out.println("Permissions already have been granted!");
        }
        finally {
            Thread.sleep(1000);
            qrScannerPageNew.scannerCloseButton.click();
            Thread.sleep(1000);
        }
    }

    @Test(dependsOnMethods = "checkQrScanner")
    public void checkSettings() throws Exception {
        homePageNew.burgerMenuButton.click(); // go to Menu
        menuPageNew.settingsButton.click(); // go to Settings

        settingsPageNew.settingsContainer.isDisplayed();
        settingsPageNew.settingsHeader.isDisplayed();

        // Biometrics
        settingsPageNew.biometricsButton.click();
//        objBiometricsPage.okButton(driverApp).click(); // FIXME MSDK: now we must swipe slider but there is no slider in app hierarchy

        // Change Passcode

        settingsPageNew.passCodeButton.click();
        passCodePageNew.backArrow.click();

        // Chat
        settingsPageNew.chatButton.click();
        Thread.sleep(1000);
        try {
            chatPageNew.backArrow.click();
        } catch (Exception e) {
            if (Config.iOS_Devices.contains(Config.Device_Type)) {
              chatPageNew.backArrowAlt.click();
            }
            else {
                ((AndroidDriver) driverApp).pressKey(new KeyEvent(AndroidKey.BACK));
            }
        }

        // About
        settingsPageNew.aboutButton.click();
        aboutPageNew.backArrow.click();
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }
}

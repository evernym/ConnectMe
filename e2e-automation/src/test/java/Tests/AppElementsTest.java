package test.java.Tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import appModules.AppInjector;
import test.java.appModules.AppUtils;
import test.java.utility.Config;
import test.java.utility.IntSetup;
import test.java.appModules.AppiumUtils;
import test.java.utility.AppDriver;

public class AppElementsTest extends IntSetup {
    Injector injector = Guice.createInjector(new AppInjector());

    private AppUtils objAppUtils = injector.getInstance(AppUtils.class);
    private test.java.pageObjects.HomePage objHomePage = injector.getInstance(test.java.pageObjects.HomePage.class);
    private test.java.pageObjects.MenuPage objMenuPage = injector.getInstance(test.java.pageObjects.MenuPage.class);
    private test.java.pageObjects.MyConnectionsPage objConnectionsPage = injector.getInstance(test.java.pageObjects.MyConnectionsPage.class);
    private test.java.pageObjects.MyCredentialsPage objCredentialsPage = injector.getInstance(test.java.pageObjects.MyCredentialsPage.class);
    private test.java.pageObjects.SettingsPage objSettingsPage = injector.getInstance(test.java.pageObjects.SettingsPage.class);
    private test.java.pageObjects.BiometricsPage objBiometricsPage = injector.getInstance(test.java.pageObjects.BiometricsPage.class);
    private test.java.pageObjects.PasscodePage objPasscodePage = injector.getInstance(test.java.pageObjects.PasscodePage.class);
    private test.java.pageObjects.ChatPage objChatPage = injector.getInstance(test.java.pageObjects.ChatPage.class);
    private test.java.pageObjects.AboutPage objAboutPage = injector.getInstance(test.java.pageObjects.AboutPage.class);
    private test.java.pageObjects.OnfidoPage objOnfidoPage = injector.getInstance(test.java.pageObjects.OnfidoPage.class);
    private test.java.pageObjects.QrScannerPage qrScannerPage = injector.getInstance(test.java.pageObjects.QrScannerPage.class);

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
//        driverApp = AppDriver.getDriver(); // old way
//        objAppUtils.openApp(driverApp); // old way
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
            try {
                menuPageNew.menuAllowButton.click();
            } catch (Exception ex) {
                menuPageNew.menuAllowButtonAlt.click();
            }
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

//        // My Connections
//        objHomePage.burgerMenuButton(driverApp).click();
//        objMenuPage.myConnectionsButton(driverApp).click();
//        objConnectionsPage.myConnectionsHeader(driverApp).isDisplayed();
//        objHomePage.scanButton(driverApp).isDisplayed();
//
//        // My Credentials
//        objHomePage.burgerMenuButton(driverApp).click();
//        objMenuPage.myCredentialsButton(driverApp).click();
//        objCredentialsPage.myCredentialsHeader(driverApp).isDisplayed();
//        objHomePage.scanButton(driverApp).isDisplayed();
//
//        // Settings
//        objHomePage.burgerMenuButton(driverApp).click();
//        objMenuPage.settingsButton(driverApp).click();
//
//        // Go Back Home
//        objHomePage.burgerMenuButton(driverApp).click();
//        objMenuPage.homeButton(driverApp).click();
    }

    @Test(dependsOnMethods = "checkMenu")
    public void checkQrScanner() throws Exception {
        objHomePage.scanButton(driverApp).isDisplayed();
        objHomePage.scanButton(driverApp).click();
        try {
            qrScannerPage.scannerAllowButton(driverApp).click();
        }
        catch (NoSuchElementException e) {
            System.out.println("Permissions already have been granted!");
        }
        finally {
            Thread.sleep(1000);
            qrScannerPage.scannerCloseButton(driverApp).click();
            Thread.sleep(1000);
        }
    }

    @Test(dependsOnMethods = "checkQrScanner")
    public void checkSettings() throws Exception {
        objHomePage.burgerMenuButton(driverApp).click(); // go to Menu
        objMenuPage.settingsButton(driverApp).click(); // go to Settings

        objSettingsPage.settingsContainer(driverApp).isDisplayed();
        objSettingsPage.settingsHeader(driverApp).isDisplayed();

        // Biometrics
        objSettingsPage.biometricsButton(driverApp).click();
//        objBiometricsPage.okButton(driverApp).click(); // FIXME MSDK: now we must swipe slider but there is no slider in app hierarchy

        // Change Passcode
        try {
            objSettingsPage.passcodeButton(driverApp).click();
        } catch (Exception e) {
            objBiometricsPage.okButton(driverApp).click();
            objSettingsPage.passcodeButton(driverApp).click();
        }
        objPasscodePage.backArrow(driverApp).click();

        // Chat
        objSettingsPage.chatButton(driverApp).click();
        Thread.sleep(1000);
        try {
            objChatPage.backArrow(driverApp).click();
        } catch (Exception e) {
            if (Config.iOS_Devices.contains(Config.Device_Type)) {
                objChatPage.backArrowAlt(driverApp).click();
            }
            else {
                ((AndroidDriver) driverApp).pressKey(new KeyEvent(AndroidKey.BACK));
            }
        }

        // About
        objSettingsPage.aboutButton(driverApp).click();
        objAboutPage.backArrow(driverApp).click();
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }
}

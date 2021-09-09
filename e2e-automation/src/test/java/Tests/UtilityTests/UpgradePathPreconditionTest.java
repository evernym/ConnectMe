package test.java.Tests.UtilityTests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;
import test.java.funcModules.ConnectionModules;
import test.java.utility.LocalContext;
import test.java.utility.Config;
import test.java.appModules.AppUtils;
import test.java.utility.BrowserDriver;
import test.java.utility.AppDriver;
import test.java.funcModules.ConnectionModules;

public class UpgradePathPreconditionTest extends IntSetup {

    @BeforeClass
    public void BeforeClassSetup() {
        reloadDriversAndPos();
    }

    @DataProvider(name = "invitationTypesSource")
    public Object[][] createData() {
        return new Object[][]{
            {connectionInvitation},
            {oobInvitation},
        };
    }

    private ConnectionModules objConnectionModules = new ConnectionModules();
    private LocalContext context = LocalContext.getInstance();
    private String connectionName;
    private final String connectionInvitation = "connection-invitation";
    private final String oobInvitation = "out-of-band-invitation";

    @Test
    public void setUpWizardTest() {
        try {
            startUpPageNew.setUpButton.click();
            passCodePageNew.enterPassCode();
            Thread.sleep(2000);
            passCodePageNew.enterPassCode();
            startUpPageNew.switchEnv();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @Test
    @Test(dependsOnMethods = "setUpWizardTest")
    public void checkHome() {
        homePageNew.checkHome();
    }

    @Test(dependsOnMethods = "checkHome")
    public void checkMenu() throws InterruptedException {
        homePageNew.tapOnBurgerMenu();
        menuPageNew.menuContainer.isDisplayed();
        menuPageNew.banner.isDisplayed();
        menuPageNew.logo.isDisplayed();
        menuPageNew.builtByFooter.isDisplayed();
        menuPageNew.versionFooter.isDisplayed();

        menuPageNew.userAvatar.click();
        menuPageNew.getGalleryPermissions();

        Thread.sleep(1000);
        if((Config.Device_Type.equals("android")|| Config.Device_Type.equals("awsAndroid"))) {
            ((AndroidDriver) driverApp).pressKey(new KeyEvent(AndroidKey.BACK));
        } else {
            menuPageNew.cancelButton.click();
        }

        menuPageNew.homeButton.click();

        homePageNew.tapOnBurgerMenu();
        menuPageNew.myConnectionsButton.click();
        myConnectionsPageNew.myConnectionsHeader.isDisplayed();
        homePageNew.scanButton.isDisplayed();

        homePageNew.tapOnBurgerMenu();
        menuPageNew.myCredentialsButton.click();
        myCredentialsPageNew.myCredentialsHeader.isDisplayed();
        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.click();

        homePageNew.scanButton.isDisplayed();

        homePageNew.tapOnBurgerMenu();
        menuPageNew.settingsButton.click();

        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.click();
        homePageNew.homeHeader.isDisplayed();
    }

    @Test(dependsOnMethods = "checkMenu")
    public void checkQrScanner() throws Exception {
        homePageNew.scanButton.isDisplayed();
        homePageNew.scanButton.click();
        qrScannerPageNew.getCameraPermissions();
        Thread.sleep(1000);
        qrScannerPageNew.scannerCloseButton.click();
        Thread.sleep(1000);
    }

    @Test(dependsOnMethods = "checkQrScanner")
    public void checkSettings() throws Exception {
        homePageNew.tapOnBurgerMenu(); // go to Menu
        menuPageNew.settingsButton.click(); // go to Settings

        settingsPageNew.settingsContainer.isDisplayed();
        settingsPageNew.settingsHeader.isDisplayed();
        settingsPageNew.biometricsButton.click();
        settingsPageNew.passCodeButton.click();
        passCodePageNew.backArrow.click();
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

        settingsPageNew.aboutButton.click();
        aboutPageNew.backArrow.click();
    }

    @Test(dataProvider = "invitationTypesSource", dependsOnMethods = "checkSettings")
    public void setUpConnectionTest(String invitationType) throws Exception {
        AppDriver.quit();
        BrowserDriver.quit();
        reloadDriversAndPos();
        objConnectionModules = new ConnectionModules();
        connectionName = invitationType;

        // Use custom iteration approach to ensure that connection would be available for the next tests
        AppUtils.DoSomethingEventuallyNew(
            () -> driverBrowser = BrowserDriver.getDriver(),
            () -> driverBrowser.launchApp(),
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, connectionName, invitationType),
            () -> objConnectionModules.acceptPushNotificationRequest(driverApp),
            () -> AppUtils.waitForElementNew(driverApp, invitationPageNew.title),
            () -> objConnectionModules.acceptConnectionInvitation(driverApp)
        );

        try {
            switch (connectionName) {
                case connectionInvitation:
                    AppUtils.waitForElementNew(driverApp, homePageNew.commonConnectedEvent);
                    break;
                case oobInvitation:
                    AppUtils.waitForElementNew(driverApp, homePageNew.oobConnectedEvent);
                    break;
            }
        } catch (Exception e) {
            System.exit(1); // don't run other tests if this fails
        }

        Thread.sleep(1000);
        BrowserDriver.closeApp();
    }

    @Test(dependsOnMethods = "setUpConnectionTest")
    public void validateMyConnectionRecordAppeared() throws Exception {
        passCodePageNew.openApp();

        homePageNew.tapOnBurgerMenu();
        menuPageNew.myConnectionsButton.click();
        Thread.sleep(1000);
        myConnectionsPageNew.getConnectionByName(connectionName).click();
    }

    @Test(dependsOnMethods = "validateMyConnectionRecordAppeared")
    public void validateConnectionHistory() throws Exception {
        connectionHistoryPageNew.connectionLogo.isDisplayed();
        connectionHistoryPageNew.oobConnectionName.isDisplayed();
        connectionHistoryPageNew.connectedRecord.isDisplayed();
        connectionHistoryPageNew.oobConnectedRecordDescription.isDisplayed();
    }

    @Test(dependsOnMethods = "validateConnectionHistory")
    public void validateConnectionDetails() throws Exception {
        connectionHistoryPageNew.threeDotsButton.isDisplayed();
        connectionHistoryPageNew.threeDotsButton.click();

        connectionDetailPageNew.closeButton.isDisplayed();
        connectionDetailPageNew.deleteButton.isDisplayed();

        connectionDetailPageNew.closeButton.click();
        connectionHistoryPageNew.backButton.click();
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }
}

package test.java.Tests.UtilityTests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.Platform;
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
import org.openqa.selenium.WebElement;

import java.util.List;

public class UpgradePathPreconditionTest extends IntSetup {
    private ConnectionModules objConnectionModules = new ConnectionModules();
    private LocalContext context = LocalContext.getInstance();

    private String connectionName;
    private final String connectionInvitation = "connection-invitation";
    private final String oobInvitation = "out-of-band-invitation";

    @BeforeClass
    public void BeforeClassSetup() {
        System.out.println("Upgrade Path Precondition Test Suite has been started!");
        driverApp.launchApp();
    }

    @DataProvider(name = "invitationTypesSource")
    public Object[][] createData() {
        return new Object[][]{
            {connectionInvitation},
            {oobInvitation},
        };
    }

    @Test(dataProvider = "invitationTypesSource")
    public void setUpConnectionTest(String invitationType) throws Exception {
        connectionName = invitationType;
        driverBrowser = BrowserDriver.getDriver();
        objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, connectionName, invitationType);
        objConnectionModules.acceptPushNotificationRequest(driverApp);

        try {
            AppUtils.waitForElementNew(driverApp, invitationPageNew.title, 10);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            AppUtils.DoSomethingEventuallyNew(15,
                () -> driverApp.terminateApp("me.connect"),
                () -> driverApp.launchApp(),
                () -> new AppUtils().authForAction(),
                () -> AppUtils.waitForElementNew(driverApp, invitationPageNew.title, 10)
            );
        }
        objConnectionModules.acceptConnectionInvitation(driverApp);

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

        if(test.java.utility.Helpers.getPlatformType().equals(Platform.ANDROID)) driverBrowser.closeApp();
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
        context.setValue("connectionName", connectionName);
        System.out.println("Connection name in context: " + connectionName);

        driverApp.closeApp();
        System.out.println("Upgrade Path Precondition Test Suite has been finished!");
    }
}

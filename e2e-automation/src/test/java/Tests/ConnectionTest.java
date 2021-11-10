package test.java.Tests;

import org.openqa.selenium.Platform;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.funcModules.ConnectionModules;
import test.java.utility.Helpers;
import test.java.utility.LocalContext;
import test.java.utility.BrowserDriver;
import test.java.utility.AppDriver;

/**
 * The ConnectionTest class is a Test class which holds test method related to
 * connection
 */
public class ConnectionTest extends IntSetup {
    private ConnectionModules objConnectionModules = new ConnectionModules();
    private LocalContext context = LocalContext.getInstance();

    private String connectionName;
    private final String connectionInvitation = "connection-invitation";
    private final String oobInvitation = "out-of-band-invitation";
    private boolean isDisplayed = false;

    @BeforeClass
    public void BeforeClassSetup() {
        System.out.println("Connection Test Suite has been started!");
        driverApp.launchApp();
    }

    @DataProvider(name = "invitationTypesSource")
    public Object[][] createData() {
        return new Object[][]{
            {connectionInvitation},
            {oobInvitation},
        };
    }

//     @Test(dataProvider = "invitationTypesSource")
//     public void rejectConnectionTest(String invitationType) throws Exception {
//         driverBrowser = BrowserDriver.getDriver();
//         objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, Helpers.randomString(), invitationType);
//         objConnectionModules.acceptPushNotificationRequest(driverApp);
//
//         try {
//             AppUtils.waitForElementNew(driverApp, invitationPageNew.title, 5);
//         }
//         catch (Exception e)
//         {
//             System.out.println(e.getMessage());
//             AppUtils.DoSomethingEventuallyNew(15,
//                 () -> driverApp.terminateApp("me.connect"),
//                 () -> driverApp.launchApp(),
//                 () -> new AppUtils().authForAction(),
//                 () -> AppUtils.waitForElementNew(driverApp, invitationPageNew.title, 5));
//         }
//         objConnectionModules.rejectConnectionInvitation(driverApp);
//
//         if(Helpers.getPlatformType().equals(Platform.ANDROID)) driverBrowser.closeApp();
//     }

    @Test(dataProvider = "invitationTypesSource")
    public void setUpConnectionTest(String invitationType) throws Exception {
        driverBrowser = BrowserDriver.getDriver();
        objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, Helpers.randomString(), invitationType);
        objConnectionModules.acceptPushNotificationRequest(driverApp);
        connectionName = invitationType;

        try {
            AppUtils.waitForElementNew(driverApp, invitationPageNew.title, 5);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            AppUtils.DoSomethingEventuallyNew(15,
                () -> driverApp.terminateApp("me.connect"),
                () -> driverApp.launchApp(),
                () -> new AppUtils().authForAction(),
                () -> AppUtils.waitForElementNew(driverApp, invitationPageNew.title, 5)
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

        if(Helpers.getPlatformType().equals(Platform.ANDROID)) driverBrowser.closeApp();

        context.setValue(invitationType + "_DID", context.getValue("DID"));
    }

    @Test(dependsOnMethods = "setUpConnectionTest")
    public void validateMyConnectionRecordAppeared() throws Exception {
        passCodePageNew.openApp();

        homePageNew.tapOnBurgerMenu();
        menuPageNew.myConnectionsButton.click();
        Thread.sleep(1000); // FIXME MSDK workaround: it goes to Settings without sleep
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
        System.out.println("Device ID in context: " + context.getValue("DID"));

        driverApp.closeApp();
        System.out.println("Connection Test Suite has been finished!");
    }
}

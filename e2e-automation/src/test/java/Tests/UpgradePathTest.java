package test.java.Tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;

public class UpgradePathTest extends IntSetup {
    private test.java.funcModules.ConnectionModules objConnectionModules = new test.java.funcModules.ConnectionModules();
    private test.java.utility.LocalContext context = test.java.utility.LocalContext.getInstance();

    private String connectionName;
    private final String connectionInvitation = "connection-invitation-upt";
    private final String oobInvitation = "out-of-band-invitation-upt";

    @BeforeClass
    public void BeforeClassSetup() {
        System.out.println("Connection Test Suite has been started!");
        driverApp.launchApp();
    }

    @DataProvider(name = "data1")
    public Object[][] createData() {
        return new Object[][]{
            {connectionInvitation},
            {oobInvitation},
        };
    }

    @Test(dataProvider = "data1")
    public void rejectConnectionTest(String invitationType) throws Exception {
        driverBrowser = test.java.utility.BrowserDriver.getDriver();

        test.java.appModules.AppUtils.DoSomethingEventuallyNew(
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, test.java.utility.Helpers.randomString(), invitationType),
            () -> objConnectionModules.acceptPushNotificationRequest(driverApp),
            () -> test.java.appModules.AppUtils.waitForElementNew(driverApp, invitationPageNew.title),
            () -> objConnectionModules.rejectConnectionInvitation(driverApp)
        );

        Thread.sleep(1000);
        test.java.utility.BrowserDriver.closeApp();
    }

    @Test(dataProvider = "data1", dependsOnMethods = "rejectConnectionTest")
    public void setUpConnectionTest(String invitationType) throws Exception {
        connectionName = invitationType;

        driverBrowser = test.java.utility.BrowserDriver.getDriver();

        test.java.appModules.AppUtils.DoSomethingEventuallyNew(
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, connectionName, invitationType),
            () -> test.java.appModules.AppUtils.waitForElementNew(driverApp, invitationPageNew.title),
            () -> objConnectionModules.acceptConnectionInvitation(driverApp)
        );

        try {
            switch (connectionName) {
                case connectionInvitation:
                    test.java.appModules.AppUtils.waitForElementNew(driverApp, homePageNew.commonConnectedEvent);
                    break;
                case oobInvitation:
                    test.java.appModules.AppUtils.waitForElementNew(driverApp, homePageNew.oobConnectedEvent);
                    break;
            }
        } catch (Exception e) {
            System.exit(1); // don't run other tests if this fails
        }

        Thread.sleep(1000);
        test.java.utility.BrowserDriver.closeApp();
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

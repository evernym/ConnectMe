package test.java.Tests;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.utility.LocalContext;
import test.java.utility.Config;
import test.java.funcModules.ConnectionModules;

import java.util.NoSuchElementException;


public class DeletionTest extends IntSetup {
    private AppUtils objAppUtils = new AppUtils();
    private ConnectionModules objConnectionModules = new ConnectionModules();
    private LocalContext context = LocalContext.getInstance();

    private String connectionInvitation = "connection-invitation";
    private String oobInvitation = "out-of-band-invitation";
    private boolean isDisplayed = false;
    private String credentialNameScheme;
    private String credentialNameManyScheme;

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
        passCodePageNew.openApp();
        credentialNameScheme = context.getValue("credentialNameScheme");
        credentialNameManyScheme = context.getValue("credentialNameManyScheme");

        System.out.println("Starting deletion tests"); }

    @Test
    public void deleteEmptyConnection() throws Exception {
        objConnectionModules.openConnectionHistory(connectionInvitation);

        AppUtils.waitForElementNew(driverApp, connectionHistoryPageNew.threeDotsButton);
        connectionHistoryPageNew.threeDotsButton.click();
        connectionDetailPageNew.deleteButton.click();

        if (Config.iOS_Devices.contains(Config.Device_Type)) { // delete button tapping ios issue
            try {
                connectionDetailPageNew.deleteButton.click();
            } catch (Exception e) { }
        }

        Thread.sleep(5000); // FIXME
        Assert.assertNull(myConnectionsPageNew.getConnectionByName(connectionInvitation));
    }

    @Test(dependsOnMethods = "deleteEmptyConnection")
    public void deleteCredentialFromExistingConnection() throws Exception {
        homePageNew.tapOnBurgerMenu();
        menuPageNew.myCredentialsButton.click();
        // TODO: move this logic to helper
        int credsCountBefore = 0;
        try {
            objAppUtils.findParameterizedElementAlt(credentialNameManyScheme).click();
            credsCountBefore = 1;
            Thread.sleep(1000);
            if(AppUtils.isElementAbsent(driverApp, connectionHistoryPageNew.threeDotsButton)) throw new NoSuchElementException();
        } catch (Exception ex) {
            AppUtils.pullScreenUp(driverApp);
            credsCountBefore = myCredentialsPageNew.getConnectionsBySchemeName(credentialNameManyScheme).size();
            objAppUtils.findParameterizedElementAlt(credentialNameManyScheme).click();
        }
        connectionHistoryPageNew.threeDotsButton.click();
        Thread.sleep(1000);
        credentialPageNew.deleteButton.click();

        if (Config.iOS_Devices.contains(Config.Device_Type)) { // delete button tapping ios issue
            try {
                credentialPageNew.deleteButton.click();
            } catch (Exception e) {

            }
        }

        Assert.assertEquals(myCredentialsPageNew.getConnectionsBySchemeName(credentialNameManyScheme).size(), 0);
    }

    @Test(dependsOnMethods = "deleteCredentialFromExistingConnection")
    public void deleteNotEmptyConnection() throws Exception {
        objConnectionModules.openConnectionHistory(oobInvitation);

        AppUtils.waitForElementNew(driverApp, connectionHistoryPageNew.threeDotsButton);
        connectionHistoryPageNew.threeDotsButton.click();
        connectionDetailPageNew.deleteButton.click();

        if (Config.iOS_Devices.contains(Config.Device_Type)) { // delete button tapping ios issue
            try {
                connectionDetailPageNew.deleteButton.click();
            } catch (Exception e) {

            }
        }

        Thread.sleep(5000); // FIXME
        Assert.assertNull(myConnectionsPageNew.getConnectionByName(oobInvitation));
    }

    @Test(dependsOnMethods = "deleteNotEmptyConnection")
    public void deleteCredentialFromDeletedConnection() throws Exception {
        homePageNew.tapOnBurgerMenu();
        menuPageNew.myCredentialsButton.click();
        // TODO: move this logic to helper
        int credsCountBefore = 0;
        try {
            objAppUtils.findParameterizedElementAlt(credentialNameScheme).click();
            Thread.sleep(1000);
            credsCountBefore = 1;
            if(AppUtils.isElementAbsent(driverApp, connectionHistoryPageNew.threeDotsButton)) throw new NoSuchElementException();
        } catch (Exception ex) {
            AppUtils.pullScreenUp(driverApp);
            credsCountBefore = myCredentialsPageNew.getConnectionsBySchemeName(credentialNameManyScheme).size();
            objAppUtils.findParameterizedElementAlt(credentialNameScheme).click();
        }
        connectionHistoryPageNew.threeDotsButton.click();
        Thread.sleep(1000);
        credentialPageNew.deleteButton.click();

        if (Config.iOS_Devices.contains(Config.Device_Type)) { // delete button tapping ios issue
            try {
                credentialPageNew.deleteButton.click();
            } catch (Exception e) {

            }
        }

        Assert.assertEquals(myCredentialsPageNew.getConnectionsBySchemeName(credentialNameScheme).size(), 0);
    }

    @AfterClass
    public void AfterClass() {
        driverApp.quit();
    }
}

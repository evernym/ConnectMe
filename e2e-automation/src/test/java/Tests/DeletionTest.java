package test.java.Tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.utility.LocalContext;
import test.java.utility.Config;


public class DeletionTest extends IntSetup {
    private AppUtils objAppUtils = new AppUtils();
    private LocalContext context = LocalContext.getInstance();

    private String connectionInvitation = "connection-invitation";
    private String oobInvitation = "out-of-band-invitation";
    private boolean isDisplayed = false;
    private String credentialName;
    private String credentialNameMany;

    @BeforeClass
    public void BeforeClassSetup() throws Exception{
        passCodePageNew.openApp();
        credentialName = context.getValue("credentialName");
        credentialNameMany = context.getValue("credentialNameMany");
    }

    @Test
    public void deleteEmptyConnection() throws Exception {
        homePageNew.burgerMenuButton.click();
        menuPageNew.myConnectionsButton.click();
        myConnectionsPageNew.testConnection(connectionInvitation).click();
        connectionHistoryPageNew.threeDotsButton.click();
        connectionDetailPageNew.deleteButton.click();

        if (Config.iOS_Devices.contains(Config.Device_Type)) { // delete button tapping ios issue
           try {
             connectionDetailPageNew.deleteButton.click();
           } catch (Exception e) {

           }
        }

        AppUtils.isNotDisplayed(
                () -> myConnectionsPageNew.testConnection(connectionInvitation).isDisplayed()
        );
    }

    @Test(dependsOnMethods = "deleteEmptyConnection")
    public void deleteCredentialFromExistingConnection() throws Exception {
        homePageNew.burgerMenuButton.click();
        menuPageNew.myCredentialsButton.click();
        // TODO: move this logic to helper
        try {
          objAppUtils.findParameterizedElement(credentialNameMany).click();
        } catch (Exception ex) {
            AppUtils.pullScreenUp(driverApp);
          objAppUtils.findParameterizedElement(credentialNameMany).click();
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

        AppUtils.isNotDisplayed(
                () -> objAppUtils.findParameterizedElement(credentialNameMany).isDisplayed()
        );
    }

    @Test(dependsOnMethods = "deleteCredentialFromExistingConnection")
    public void deleteNotEmptyConnection() throws Exception {
        homePageNew.burgerMenuButton.click();
        menuPageNew.myConnectionsButton.click();
        myConnectionsPageNew.testConnection(oobInvitation).click();
        connectionHistoryPageNew.threeDotsButton.click();
        Thread.sleep(1000);
        connectionDetailPageNew.deleteButton.click();

        if (Config.iOS_Devices.contains(Config.Device_Type)) { // delete button tapping ios issue
            try {
              connectionDetailPageNew.deleteButton.click();
            } catch (Exception e) {

            }
        }

        AppUtils.isNotDisplayed(
                () -> myConnectionsPageNew.testConnection(oobInvitation).isDisplayed()
        );
    }

    @Test(dependsOnMethods = "deleteNotEmptyConnection")
    public void deleteCredentialFromDeletedConnection() throws Exception {
        homePageNew.burgerMenuButton.click();
        menuPageNew.myCredentialsButton.click();
        // TODO: move this logic to helper
        try {
          objAppUtils.findParameterizedElement(credentialName).click();
        } catch (Exception ex) {
            AppUtils.pullScreenUp(driverApp);
          objAppUtils.findParameterizedElement(credentialName).click();
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

        AppUtils.isNotDisplayed(
                () -> objAppUtils.findParameterizedElement(credentialName).isDisplayed()
        );
    }

    @AfterClass
    public void AfterClass() {
        driverApp.quit();
    }
}

package test.java.Tests;

import appModules.AppInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.java.utility.AppDriver;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.appModules.VASApi;
import test.java.pageObjects.HomePage;
import test.java.pageObjects.MenuPage;
import test.java.pageObjects.MyConnectionsPage;
import test.java.pageObjects.MyCredentialsPage;
import test.java.pageObjects.CredentialPage;
import test.java.pageObjects.ConnectionHistoryPage;
import test.java.pageObjects.ConnectionDetailPage;
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
            myCredentialsPageNew.findParameterizedElement(credentialNameMany).click();
        } catch (Exception ex) {
            AppUtils.pullScreenUp(driverApp);
          myCredentialsPageNew.findParameterizedElement(credentialNameMany).click();
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
                () -> myCredentialsPageNew.findParameterizedElement(credentialNameMany).isDisplayed()
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
            myCredentialsPageNew.findParameterizedElement(credentialName).click();
        } catch (Exception ex) {
            AppUtils.pullScreenUp(driverApp);
          myCredentialsPageNew.findParameterizedElement(credentialName).click();
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
                () -> myCredentialsPageNew.findParameterizedElement(credentialName).isDisplayed()
        );
    }

    @AfterClass
    public void AfterClass() {
        driverApp.quit();
    }
}

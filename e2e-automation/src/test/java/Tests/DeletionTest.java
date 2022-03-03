package test.java.Tests;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.appModules.AppUtils;
import test.java.utility.IntSetup;
import test.java.utility.LocalContext;
import test.java.utility.Config;
import test.java.funcModules.ConnectionModules;

import java.util.List;
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

//        Thread.sleep(5000); // FIXME

//        new WebDriverWait(driverApp, 10, 1000)
//            .until(ExpectedConditions.visibilityOf(myConnectionsPageNew.myConnectionsHeader));
//            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[@name=\"My Connections\"]")));
        Assert.assertNull(myConnectionsPageNew.getConnectionByName(connectionInvitation));
    }

    @Test(dependsOnMethods = "deleteEmptyConnection")
    public void deleteCredentialFromExistingConnection() throws Exception {
        WebDriverWait wait = new WebDriverWait(driverApp, 3, 500);
        homePageNew.tapOnBurgerMenu();
        menuPageNew.myCredentialsButton.click();

        // === the code that does not work yet
        // it does not work: actual 1; expected 0;
        List<WebElement> credentialsBefore = myCredentialsPageNew.getCredentialsBySchemeName(credentialNameManyScheme);
        myCredentialsPageNew.expandCredentialBySchemeName(credentialNameManyScheme);
//        if (credentialsBefore.size() > 0) {
//            credentialsBefore.get(0).click();
//        } else {
//            throw new Exception("No credentials with name " + credentialNameScheme + " has been found");
//            // not really needed; the test will fail if three dots are not found
//            // if no credentials, that means pre-test failed, not the test.
//        }
//        if (credentialsBefore.size() > 1) {
//            credentialsBefore.get(0).click();
//        }

        wait
            .until(ExpectedConditions.elementToBeClickable(connectionHistoryPageNew.threeDotsButton))
            .click();
        wait
            .until(ExpectedConditions.elementToBeClickable(credentialPageNew.deleteButton))
            .click();
        if (test.java.utility.Helpers.getPlatformType().equals(Platform.IOS)) { // delete button tapping ios issue
            try {
                credentialPageNew.deleteButton.click();
            } catch (Exception e) { // got here for the last test run
                System.out.println("DeletionTest >" +
                    " deleteCredentialFromExistingConnection >" +
                    " iOS delete button tapping issue has not appeared");
            }
        }
        List<WebElement> credentialsAfter = myCredentialsPageNew.getCredentialsBySchemeName(credentialNameManyScheme);
        System.out.println(credentialNameScheme);
        System.out.println(credentialsBefore.size());
        System.out.println(credentialsAfter.size());
        Assert.assertEquals(credentialsAfter.size(), credentialsBefore.size() - 1);
        // ^^^ the code that does not work yet

        // TODO: move this logic to helper (to credential page? - no, to app utils; may cause exception if invoked in the wrong place)
        //why app utils is not static?

        //=====the code to refactor that works====
//        int credsCountBefore = 0
//        try {
//            objAppUtils.findParameterizedElementAlt(credentialNameManyScheme).click();
//            credsCountBefore = 1;
//            Thread.sleep(1000);
//            if(AppUtils.isElementAbsent(driverApp, connectionHistoryPageNew.threeDotsButton)) throw new NoSuchElementException();
//        } catch (Exception ex) {
//            AppUtils.pullScreenUp(driverApp);
//            credsCountBefore = myCredentialsPageNew.getConnectionsBySchemeName(credentialNameManyScheme).size();
//            objAppUtils.findParameterizedElementAlt(credentialNameManyScheme).click();
//        }
//        connectionHistoryPageNew.threeDotsButton.click(); //why the button is clicked on connectionHistory is a connection has alreqdy been clicked, current page - credential page?
//        Thread.sleep(1000);
//        credentialPageNew.deleteButton.click();
//
//        if (Config.iOS_Devices.contains(Config.Device_Type)) { // delete button tapping ios issue
//            try {
//                credentialPageNew.deleteButton.click();
//            } catch (Exception e) {
//
//            }
//        }
//        Assert.assertEquals(myCredentialsPageNew.getConnectionsBySchemeName(credentialNameManyScheme).size(), 0);
        //^^^^the code to refactor that works^^^^

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

//        Thread.sleep(5000); // FIXME

//        new WebDriverWait(driverApp, 5, 500)
//            .until(ExpectedConditions.visibilityOf(myConnectionsPageNew.myConnectionsHeader));

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

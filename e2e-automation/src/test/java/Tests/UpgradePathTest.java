package test.java.Tests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObjects.*;
import test.java.appModules.AppUtils;
import test.java.appModules.VASApi;
import test.java.utility.Config;
import test.java.utility.Helpers;
import test.java.utility.IntSetup;
import test.java.utility.LocalContext;
import test.java.funcModules.ConnectionModules;
import test.java.utility.AppDriver;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class UpgradePathTest extends IntSetup {
    private ConnectionModules objConnectionModules = new ConnectionModules();
    private LocalContext context = LocalContext.getInstance();

    private final String connectionInvitation = "connection-invitation";
    private final String oobConnection = "out-of-band-invitation";
    private VASApi VAS;
    private AppUtils objAppUtlis;
    String DID;
    String connectionName;

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
        reloadDriversAndPos();
        DID = context.getValue("DID");
        connectionName = context.getValue("connectionName");

        VAS = VASApi.getInstance();
        objAppUtlis = new AppUtils();

        passCodePageNew.openApp();
    }

    @Test
    public void checkHome() {
        homePageNew.checkHome();
    }

    @Test(dependsOnMethods = "checkHome")
    public void acceptCredentialFromHome() throws Exception {
        String credentialName = Helpers.randomString();
        if (Config.iOS_Devices.contains(Config.Device_Type)) {
            context.setValue("credDefId", "WPz8oRna9NGVyhK29fTbKa:3:CL:201655:tag");
        } else {
            context.setValue("credDefId", "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:201654:tag");
        }

        System.out.println("Credential name: " + credentialName);
        context.setValue("credentialName", credentialName);

        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.isDisplayed();
        menuPageNew.homeButton.click();

        AppUtils.DoSomethingEventually(
            () -> VAS.sendCredentialOffer(DID, context.getValue("credDefId"), test.java.utility.Constants.values, credentialName)
        );

        AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader);
        String schemeName = credentialPageNew.credentialSchemeName.getText();
        context.setValue("credentialNameScheme", schemeName);
        objAppUtlis.acceptCredential();
        homePageNew.recentEventsSection.isDisplayed();
        AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(schemeName));
    }

    @Test(dependsOnMethods = "acceptCredentialFromHome")
    public void shareProofRequestContainingGroupedAttributes() throws Exception {
        String attribute1 = "FirstName";
        String attribute2 = "LastName";

        List<JSONObject> requestedAttributes = Arrays.asList(
            new JSONObject().put("names", Arrays.asList(attribute1, attribute2))
        );

        String proofName = Helpers.randomString();
        AppUtils.DoSomethingEventually(
            () -> VAS.requestProof(DID, proofName, requestedAttributes, null)
        );

        AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader); // option 2
        objAppUtlis.findParameterizedElement(attribute1).isDisplayed();
        objAppUtlis.findParameterizedElement(attribute2).isDisplayed();
        objAppUtlis.shareProof();
        AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
    }
//
//    @Test(dependsOnMethods = "shareProofRequestContainingGroupedAttributes")
//    public void deleteCredentialFromExistingConnection() {
//        String schemeName = context.getValue("credentialNameScheme");
//
//        homePageNew.tapOnBurgerMenu();
//        menuPageNew.myCredentialsButton.click();
//
//        int credsCountBefore = 0;
//        try {
//            credsCountBefore = 1;
//            objAppUtlis.findParameterizedElementAlt(schemeName).click();
//            Thread.sleep(1000);
//            if (AppUtils.isElementAbsent(driverApp, connectionHistoryPageNew.threeDotsButton))
//                throw new NoSuchElementException();
//        } catch (Exception ex) {
//            AppUtils.pullScreenUp(driverApp);
//            credsCountBefore = myCredentialsPageNew.getConnectionsBySchemeName(schemeName).size();
//            objAppUtlis.findParameterizedElementAlt(schemeName).click();
//        }
//
//        connectionHistoryPageNew.threeDotsButton.click();
//        credentialPageNew.deleteButton.isDisplayed();
//        credentialPageNew.deleteButton.click();
//        Assert.assertEquals(myCredentialsPageNew.getConnectionsBySchemeName(schemeName).size(), credsCountBefore - 1);
//    }
//
//    @Test(dependsOnMethods = "deleteCredentialFromExistingConnection")
//    public void answerQuestionWithOneOptionFromHome() throws Exception {
//        List<String> oneOption = Arrays.asList(Helpers.randomString());
//        String text = "How much?";
//        String detail = "How much do you want";
//
//        homePageNew.tapOnBurgerMenu();
//        menuPageNew.homeButton.isDisplayed();
//        menuPageNew.homeButton.click();
//
//        AppUtils.DoSomethingEventually(() -> VAS.askQuestion(DID, text, detail, oneOption));
//        AppUtils.waitForElementNew(driverApp, questionPageNew.header);
//        objAppUtlis.findParameterizedElement(oobConnection).isDisplayed();
//        objAppUtlis.findParameterizedElement(detail).isDisplayed();
//
//        for (String validResponse : oneOption) {
//            objAppUtlis.findParameterizedElement(validResponse).isDisplayed();
//        }
//
//        String answer = oneOption.get(0);
//        objAppUtlis.findParameterizedElement(answer).click();
//        homePageNew.questionRespondedEvent(answer).isDisplayed();
//    }
//
//    @Test(dependsOnMethods = "answerQuestionWithOneOptionFromHome")
//    public void deleteConnectionTest() throws Exception {
//        homePageNew.tapOnBurgerMenu();
//        menuPageNew.myConnectionsButton.click();
//        Thread.sleep(1000);
//        myConnectionsPageNew.getConnectionByName(connectionInvitation).isDisplayed();
//        myConnectionsPageNew.getConnectionByName(oobConnection).isDisplayed();
//
//        homePageNew.tapOnBurgerMenu();
//        menuPageNew.myConnectionsButton.click();
//
//        myConnectionsPageNew.getConnectionByName(connectionInvitation).isDisplayed();
//        myConnectionsPageNew.getConnectionByName(connectionInvitation).click();
//
//        AppUtils.waitForElementNew(driverApp, connectionHistoryPageNew.threeDotsButton);
//        connectionHistoryPageNew.threeDotsButton.click();
//        connectionDetailPageNew.deleteButton.isDisplayed();
//        connectionDetailPageNew.deleteButton.click();
//        Assert.assertNull(myConnectionsPageNew.getConnectionByName(connectionInvitation));
//    }

    /*
    TODO: investingate why these tests fail only in deivcefarm
    @Test(dependsOnMethods = "deleteConnectionTest")
    public void rejectConnectionTest() throws Exception {

        driverBrowser = BrowserDriver.getDriver();
        AppUtils.DoSomethingEventually(
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, Helpers.randomString(), connectionInvitation)
        );

        objConnectionModules.acceptPushNotificationRequest(driverApp);
        AppUtils.waitForElementNew(driverApp, invitationPageNew.title);
        objConnectionModules.rejectConnectionInvitation(driverApp);
    }

    @Test(dependsOnMethods = "deleteConnectionTest")
    public void setUpConnectionTest() throws Exception {
        driverBrowser = BrowserDriver.getDriver();

        AppUtils.DoSomethingEventually(
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, newConnectionName, connectionInvitation)
        );
        AppUtils.waitForElementNew(driverApp, invitationPageNew.title);
        objConnectionModules.acceptConnectionInvitation(driverApp);
        AppUtils.waitForElementNew(driverApp, homePageNew.namedConnectionEvent(newConnectionName));
    }

    @Test(dependsOnMethods = "setUpConnectionTest")
    public void validateMyConnectionRecordAppeared() throws Exception {
        passCodePageNew.openApp();
        homePageNew.tapOnBurgerMenu();
        menuPageNew.myConnectionsButton.click();
        Thread.sleep(1000);
        myConnectionsPageNew.getConnectionByName(newConnectionName).isDisplayed();
        myConnectionsPageNew.getConnectionByName(newConnectionName).click();
        connectionHistoryPageNew.connectionLogo.isDisplayed();
        connectionHistoryPageNew.backButton.click();
        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.isDisplayed();
        menuPageNew.homeButton.click();
    }
    */

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }
}

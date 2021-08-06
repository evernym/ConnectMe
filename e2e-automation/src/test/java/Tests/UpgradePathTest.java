package test.java.Tests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;
import test.java.appModules.AppUtils;
import test.java.appModules.VASApi;
import test.java.utility.Helpers;
import test.java.utility.BrowserDriver;
import test.java.utility.Config;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class UpgradePathTest extends IntSetup {
    private test.java.funcModules.ConnectionModules objConnectionModules = new test.java.funcModules.ConnectionModules();
    private test.java.utility.LocalContext context = test.java.utility.LocalContext.getInstance();

    private String connectionName;
    private final String connectionInvitation = "connection-invitation";
    private final String oobConnection = "out-of-band-invitation";
    private VASApi VAS;
    private AppUtils objAppUtlis;

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
        VAS = VASApi.getInstance();
        objAppUtlis = new AppUtils();

        passCodePageNew.openApp();
    }

    @Test
    public void checkHome() {
        homePageNew.checkHome();
    }

    @Test(dependsOnMethods = "checkHome")
    public void deleteConnectionTest() throws Exception {
        homePageNew.tapOnBurgerMenu();
        menuPageNew.myConnectionsButton.click();

        myConnectionsPageNew.getConnectionByName(oobConnection).isDisplayed();
        myConnectionsPageNew.getConnectionByName(oobConnection).click();

        AppUtils.waitForElementNew(driverApp, connectionHistoryPageNew.threeDotsButton);
        connectionHistoryPageNew.threeDotsButton.click();
        connectionDetailPageNew.deleteButton.isDisplayed();
        connectionDetailPageNew.deleteButton.click();
        Assert.assertNull(myConnectionsPageNew.getConnectionByName(oobConnection));
    }

    @Test(dependsOnMethods = "deleteConnectionTest")
    @Ignore
    public void rejectConnectionTest() throws Exception {
        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.isDisplayed();
        menuPageNew.homeButton.click();

        driverBrowser = test.java.utility.BrowserDriver.getDriver();

        test.java.appModules.AppUtils.DoSomethingEventuallyNew(
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, test.java.utility.Helpers.randomString(), connectionInvitation),
            () -> objConnectionModules.acceptPushNotificationRequest(driverApp),
            () -> test.java.appModules.AppUtils.waitForElementNew(driverApp, invitationPageNew.title),
            () -> objConnectionModules.rejectConnectionInvitation(driverApp)
        );

        Thread.sleep(1000);
        test.java.utility.BrowserDriver.closeApp();
    }

    @Test(dependsOnMethods = "deleteConnectionTest")
    public void setUpConnectionTest() throws Exception {
        connectionName = oobConnection;
        driverBrowser = test.java.utility.BrowserDriver.getDriver();

        test.java.appModules.AppUtils.DoSomethingEventuallyNew(
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, connectionName, oobConnection),
            () -> test.java.appModules.AppUtils.waitForElementNew(driverApp, invitationPageNew.title),
            () -> objConnectionModules.acceptConnectionInvitation(driverApp)
        );

        test.java.appModules.AppUtils.waitForElementNew(driverApp, homePageNew.oobConnectedEvent);
        Thread.sleep(1000);
        BrowserDriver.closeApp();
    }

    @Test(dependsOnMethods = "setUpConnectionTest")
    public void validateMyConnectionRecordAppeared() throws Exception {
        passCodePageNew.openApp();
        homePageNew.tapOnBurgerMenu();
        menuPageNew.myConnectionsButton.click();
        myConnectionsPageNew.getConnectionByName(connectionName).isDisplayed();
        myConnectionsPageNew.getConnectionByName(connectionName).click();
        connectionHistoryPageNew.connectionLogo.isDisplayed();
        connectionHistoryPageNew.backButton.click();
        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.isDisplayed();
        menuPageNew.homeButton.click();
    }

    @Test(dependsOnMethods = "validateMyConnectionRecordAppeared")
    public void acceptCredentialFromHome() throws Exception {
        String DID = context.getValue("DID");
        String credentialName = Helpers.randomString();
        if (Config.iOS_Devices.contains(Config.Device_Type)) {
            context.setValue("credDefId", "WPz8oRna9NGVyhK29fTbKa:3:CL:201655:tag");
        } else {
            context.setValue("credDefId", "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:201654:tag");
        }

        System.out.println("Credential name: " + credentialName);
        context.setValue("credentialName", credentialName);

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
        String DID = context.getValue("DID");

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

    @Test(dependsOnMethods = "shareProofRequestContainingGroupedAttributes")
    public void deleteCredentialFromExistingConnection() {
        String credentialName = context.getValue("credentialName");
        String schemeName = context.getValue("credentialNameScheme");

        homePageNew.tapOnBurgerMenu();
        menuPageNew.myCredentialsButton.click();

        int credsCountBefore = 0;
        try {
            objAppUtlis.findParameterizedElementAlt(schemeName).click();
            credsCountBefore = 1;
            Thread.sleep(1000);
            if (AppUtils.isElementAbsent(driverApp, connectionHistoryPageNew.threeDotsButton))
                throw new NoSuchElementException();
        } catch (Exception ex) {
            AppUtils.pullScreenUp(driverApp);
            credsCountBefore = myCredentialsPageNew.getConnectionsBySchemeName(schemeName).size();
            objAppUtlis.findParameterizedElementAlt(schemeName).click();
        }

        connectionHistoryPageNew.threeDotsButton.click();
        credentialPageNew.deleteButton.isDisplayed();
        credentialPageNew.deleteButton.click();
        Assert.assertEquals(myCredentialsPageNew.getConnectionsBySchemeName(schemeName).size(), credsCountBefore - 1);
    }

    @Test(dependsOnMethods = "deleteCredentialFromExistingConnection")
    public void answerQuestionWithOneOptionFromHome() throws Exception {
        String DID = context.getValue("DID");
        List<String> oneOption = Arrays.asList(Helpers.randomString());
        String text = "How much?";
        String detail = "How much do you want";

        homePageNew.tapOnBurgerMenu();
        menuPageNew.homeButton.isDisplayed();
        menuPageNew.homeButton.click();

        AppUtils.DoSomethingEventually(() -> VAS.askQuestion(DID, text, detail, oneOption));
        AppUtils.waitForElementNew(driverApp, questionPageNew.header);

        objAppUtlis.findParameterizedElement(oobConnection).isDisplayed();
        objAppUtlis.findParameterizedElement(detail).isDisplayed();

        for (String validResponse : oneOption) {
            objAppUtlis.findParameterizedElement(validResponse).isDisplayed();
        }

        String answer = oneOption.get(0);
        objAppUtlis.findParameterizedElement(answer).click();
        homePageNew.questionRespondedEvent(answer).isDisplayed();
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
    }
}

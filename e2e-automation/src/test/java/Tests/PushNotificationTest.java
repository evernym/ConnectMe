package test.java.Tests;

import io.appium.java_client.android.AndroidDriver;
import org.json.JSONObject;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;
import test.java.appModules.AppUtils;
import test.java.appModules.VASApi;
import test.java.utility.IntSetup;
import test.java.funcModules.ConnectionModules;
import test.java.utility.Helpers;
import test.java.utility.LocalContext;
import test.java.utility.BrowserDriver;
import test.java.utility.Constants;
import test.java.utility.Config;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PushNotificationTest extends IntSetup {
    private AppUtils objAppUtlis = new AppUtils();
    private ConnectionModules objConnectionModules = new ConnectionModules();

    private VASApi VAS = VASApi.getInstance();
    private LocalContext context = LocalContext.getInstance();

    String connectionName = "push-connection-invitation-" + Helpers.randomString();
    String invitationType = "connection-invitation";
    final String appBackgroundLocked = "background + locked";
    final String appBackground = "background";

    @DataProvider(name = "appStates")
    public Object[][] getAppStates() {
        return new Object[][]{
            {appBackgroundLocked},
            {appBackground},
        };
    }

    @BeforeClass
    public void BeforeClassSetup() throws Exception {
        System.out.println("Push Notification Test Suite has been started!");
        driverBrowser = BrowserDriver.getDriver();

        if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS")) || Config.Device_Type.equals("iOSSimulator"))
            return;

        AppUtils.DoSomethingEventually(
            () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, connectionName, invitationType),
            () -> objConnectionModules.acceptConnectionInvitation(driverApp)
        );
        context.setValue("connectionName", connectionName);

        AppUtils.waitForElementNew(driverApp, homePageNew.namedConnectionEvent(connectionName));
    }


    @Test(dataProvider = "appStates")
    public void checkCredOfferNotificationAppRunningInBackground(String appState) throws Exception {
        if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS")) || Config.Device_Type.equals("iOSSimulator"))
            return;

        String credentialName = Helpers.randomString();

        switch (appState) {
            case appBackgroundLocked:
                driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
                try {
                    ((AndroidDriver) driverApp).lockDevice();
                } catch (WebDriverException e) {
                    // TODO: this bug has been fixed in 6.0+ client version
                    if (!((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver) driverApp).lockDevice();
                }
                break;
            case appBackground:
                driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
                break;
        }

        VAS.sendCredentialOffer(context.getValue("DID"), "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:185320:tag", Constants.values, credentialName);

        if (((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver) driverApp).unlockDevice();

        ((AndroidDriver) driverApp).openNotifications();

        AppUtils.DoSomethingEventually(
            () -> homePageNew.credentialOfferNotification.click()
        );

        driverApp.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        try {
            objAppUtlis.unlockApp(driverApp);
        } catch (Exception ex) {
            System.out.println("Unlocking is not needed here!");
        }

        try {
            homePageNew.newMessage.click();
        } catch (Exception ex) {
            System.out.println("New message tapping is not needed here!");
        }
        driverApp.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialSenderLogo);
        String schemeName = credentialPageNew.credentialSchemeName.getText();
        objAppUtlis.acceptCredential();
        homePageNew.recentEventsSection.isDisplayed();
        AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(schemeName));
    }

    @Test(dataProvider = "appStates")
    public void checkProofRequestNotificationAppRunningInBackground(String appState) throws Exception {
        if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS")) || Config.Device_Type.equals("iOSSimulator"))
            return;

        switch (appState) {
            case appBackgroundLocked:
                driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
                try {
                    ((AndroidDriver) driverApp).lockDevice();
                } catch (WebDriverException e) {
                    // TODO: this bug has been fixed in 6.0+ client version
                    if (!((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver) driverApp).lockDevice();
                }
                break;
            case appBackground:
                driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
                break;
        }

        String attribute1 = "FirstName";
        String attribute2 = "LastName";
        List<JSONObject> requestedAttributes = Arrays.asList(new JSONObject().put("names", Arrays.asList(attribute1, attribute2)));

        String proofName = Helpers.randomString();

        VAS.requestProof(context.getValue("DID"), proofName, requestedAttributes, null);

        if (((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver) driverApp).unlockDevice();

        ((AndroidDriver) driverApp).openNotifications();

        AppUtils.DoSomethingEventually(
            () -> homePageNew.proofRequestNotification.click()
        );

        driverApp.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        try {
            objAppUtlis.unlockApp(driverApp);
        } catch (Exception ex) {
            System.out.println("Unlocking is not needed here!");
        }

        try {
            homePageNew.newMessage.click();
        } catch (Exception ex) {
            System.out.println("New message tapping is not needed here!");
        }
        driverApp.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        objAppUtlis.shareProof();
        homePageNew.recentEventsSection.isDisplayed();
        AppUtils.waitForElement(driverApp, () -> homePageNew.proofSharedEvent(proofName)).isDisplayed();
    }

    @Test(dataProvider = "appStates")
    public void checkStructuredMessageNotificationAppRunningInBackground(String appState) throws Exception {
        if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS")) || Config.Device_Type.equals("iOSSimulator"))
            return;

        String text = "How much?";
        String detail = "How much do you want";
        List<String> option = Arrays.asList(Helpers.randomString());
        String DID = context.getValue("DID");

        switch (appState) {
            case appBackgroundLocked:
                driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
                try {
                    ((AndroidDriver) driverApp).lockDevice();
                } catch (WebDriverException e) {
                    // TODO: this bug has been fixed in 6.0+ client version
                    if (!((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver) driverApp).lockDevice();
                }
                break;
            case appBackground:
                driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
                break;
        }

        VAS.askQuestion(DID, text, detail, option);

        if (((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver) driverApp).unlockDevice();

        ((AndroidDriver) driverApp).openNotifications();

        AppUtils.DoSomethingEventually(
            () -> homePageNew.questionNotification.click()
        );

        driverApp.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        try {
            objAppUtlis.unlockApp(driverApp);
        } catch (Exception ex) {
            System.out.println("Unlocking is not needed here!");
        }

        try {
            homePageNew.newMessage.click();
        } catch (Exception ex) {
            System.out.println("New message tapping is not needed here!");
        }
        driverApp.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        AppUtils.waitForElementNew(driverApp, questionPageNew.header);
        questionPageNew.senderLogo.isDisplayed();
        objAppUtlis.findParameterizedElement(context.getValue("connectionName")).isDisplayed();
        objAppUtlis.findParameterizedElement(text).isDisplayed();
        objAppUtlis.findParameterizedElement(detail).isDisplayed();
    }

    @AfterClass
    public void AfterClass() {
        BrowserDriver.closeApp();
        driverApp.closeApp();
        System.out.println("Push Notification Test Suite has been finished!");
    }
}

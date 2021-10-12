package test.java.Tests;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import org.json.JSONObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;
import test.java.appModules.AppUtils;
import test.java.appModules.VASApi;
import test.java.utility.IntSetup;
import test.java.utility.Helpers;
import test.java.utility.LocalContext;
import test.java.utility.Constants;
import test.java.utility.Config;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;


public class PushNotificationTest extends IntSetup {
    private AppUtils objAppUtlis = new AppUtils();
    private VASApi VAS = VASApi.getInstance();
    private LocalContext context = LocalContext.getInstance();

    String connectionName;
    String DID;
    String deviceModel;
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
        connectionName = "connection-invitation";
        DID = context.getValue(connectionName + "_DID");
        deviceModel = driverApp.getCapabilities().getCapability("deviceModel").toString();
        passCodePageNew.openApp();

        // TODO: remove later
        System.out.println(">>>>>>>>>>>>>>>> Device info");
        System.out.println("Device name: " + driverApp.getCapabilities().getCapability("deviceName").toString());
        System.out.println("Device model: " + driverApp.getCapabilities().getCapability("deviceModel").toString());
        System.out.println("Cap type:version: " + driverApp.getCapabilities().getCapability("CapabilityType.VERSION").toString());

        System.out.println("Shell model: " + driverApp.executeScript("mobile: shell", ImmutableMap.of("command", "getprop ro.product.model")).toString());
        System.out.println("Shell product name: " + driverApp.executeScript("mobile: shell", ImmutableMap.of("command", "getprop ro.product.name")).toString());
        System.out.println("Shell product device: " +driverApp.executeScript("mobile: shell", ImmutableMap.of("command", "getprop ro.product.device")).toString());

    }


    @Test(dataProvider = "appStates")
    public void checkCredOfferNotificationAppRunningInBackground(String appState) throws Exception {
        if (Helpers.getPlatformType().equals(Platform.IOS) || deviceModel.equals("Pixel 2 XL")) return;

        String credentialName = Helpers.randomString();

        switch (appState) {
            case appBackgroundLocked:
                driverApp.runAppInBackground(Duration.ofSeconds(-1));
                ((AndroidDriver) driverApp).lockDevice();
                break;
            case appBackground:
                driverApp.runAppInBackground(Duration.ofSeconds(-1));
                break;
        }

        VAS.sendCredentialOffer(DID, "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:185320:tag", Constants.values, credentialName);

        if (((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver) driverApp).unlockDevice();

        ((AndroidDriver) driverApp).openNotifications();
        homePageNew.credentialOfferNotification.click();
        objAppUtlis.authForAction();

        AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialSenderLogo);
        String schemeName = credentialPageNew.credentialSchemeName.getText();
        objAppUtlis.acceptCredential();
        homePageNew.recentEventsSection.isDisplayed();
        AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(schemeName));
    }

    @Test(dataProvider = "appStates", dependsOnMethods = "checkCredOfferNotificationAppRunningInBackground")
    public void checkProofRequestNotificationAppRunningInBackground(String appState) throws Exception {
        if (Helpers.getPlatformType().equals(Platform.IOS) || deviceModel.equals("Pixel 2 XL")) return;

        switch (appState) {
            case appBackgroundLocked:
                driverApp.runAppInBackground(Duration.ofSeconds(-1));
                ((AndroidDriver) driverApp).lockDevice();
                break;
            case appBackground:
                driverApp.runAppInBackground(Duration.ofSeconds(-1));
                break;
        }

        String attribute1 = "FirstName";
        String attribute2 = "LastName";
        List<JSONObject> requestedAttributes = Arrays.asList(new JSONObject().put("names", Arrays.asList(attribute1, attribute2)));
        String proofName = Helpers.randomString();

        VAS.requestProof(DID, proofName, requestedAttributes, null);
        if (((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver)driverApp).unlockDevice();
        ((AndroidDriver) driverApp).openNotifications();
        homePageNew.proofRequestNotification.click();

        objAppUtlis.authForAction();
        objAppUtlis.shareProof();
        homePageNew.recentEventsSection.isDisplayed();
        AppUtils.waitForElement(driverApp, () -> homePageNew.proofSharedEvent(proofName)).isDisplayed();
    }

    @Test(dataProvider = "appStates", dependsOnMethods = "checkProofRequestNotificationAppRunningInBackground")
    public void checkStructuredMessageNotificationAppRunningInBackground(String appState) throws Exception {
        if (Helpers.getPlatformType().equals(Platform.IOS) || deviceModel.equals("Pixel 2 XL")) return;

        String text = "How much?";
        String detail = "How much do you want";
        List<String> option = Arrays.asList(Helpers.randomString());

        switch (appState) {
            case appBackgroundLocked:
                driverApp.runAppInBackground(Duration.ofSeconds(-1));
                ((AndroidDriver) driverApp).lockDevice();
                break;
            case appBackground:
                driverApp.runAppInBackground(Duration.ofSeconds(-1));
                break;
        }

        VAS.askQuestion(DID, text, detail, option);

        if (((AndroidDriver) driverApp).isDeviceLocked()) ((AndroidDriver)driverApp).unlockDevice();
        ((AndroidDriver) driverApp).openNotifications();
        homePageNew.questionNotification.click();

        objAppUtlis.authForAction();
        AppUtils.waitForElementNew(driverApp, questionPageNew.header);
        questionPageNew.senderLogo.isDisplayed();
        objAppUtlis.findParameterizedElement(connectionName).isDisplayed();
        objAppUtlis.findParameterizedElement(text).isDisplayed();
        objAppUtlis.findParameterizedElement(detail).isDisplayed();
    }

    @AfterClass
    public void AfterClass() {
        driverApp.closeApp();
        System.out.println("Push Notification Test Suite has been finished!");
    }
}

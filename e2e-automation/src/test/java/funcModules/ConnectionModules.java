package test.java.funcModules;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.TouchAction;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.Dimension;
import test.java.utility.IntSetup;
import test.java.appModules.VASApi;
import test.java.appModules.AcaPyApi;
import test.java.utility.Config;
import test.java.utility.AppDriver;
import test.java.utility.LocalContext;
import test.java.appModules.AppUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The ConnectionModules class is to implement method related to connection
 */
public class ConnectionModules extends IntSetup {
    /**
     * install the app from hockeyapp by sms link
     *
     * @param driver - appium driver available for session
     * @param link   - sms link for which we will install app
     * @return void
     */
    public void installApp(AppiumDriver driver, String link) throws Exception {

        if (Config.Device_Type.equals("iOS")) {
            driver.get(link);
            Thread.sleep(5000);
//			-----
//			hockeyAppPage.userNameText(driver).click();
//			driver.getKeyboard().sendKeys("ankur.mishra@evernym.com");
//			hockeyAppPage.passwordText(driver).click();
//			driver.getKeyboard().sendKeys("evernymzmr123$");
//			hockeyAppPage.signinButton(driver).click();
//			String InstallConnectMeLink = hockeyAppPage.installButton(driver).getAttribute("href");
//			Config.BuildNo = hockeyAppPage.appVersion(driver).getText().substring(13, 16);
//			System.out.println("build no" + Config.BuildNo);
//			driver.get(InstallConnectMeLink);
//			driver.switchTo().alert().accept();
        } else if (Config.Device_Type.equals("android")) {
            if (!link.equals("")) {
                driver.get(link);
                Thread.sleep(3000);
//				((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
            RestAssured.baseURI = "https://api.appcenter.ms/v0.1/apps";
            final String owner = "/build-zg6l";
//			final String owner = "/Evernym-Inc";
            final String app = "/QA-MeConnect-Android";
//			final String app = "/Dev-MeConnect-Android";
            final String postfix = "/releases/latest";
            final String token = System.getenv("TOKEN");
            String InstallConnectMeLink = RestAssured
                .given()
                .header("X-API-Token", token)
                .when().get(owner + app + postfix)
                .then().statusCode(200)
                .extract().path("install_url");
            System.out.println(InstallConnectMeLink);
            driver.installApp(InstallConnectMeLink);
//			-----
//			hockeyAppPage.userNameText(driver).click();
//			driver.getKeyboard().sendKeys("ankur.mishra@evernym.com");
//			hockeyAppPage.passwordText(driver).click();
//			driver.getKeyboard().sendKeys("evernymzmr123$");
//			hockeyAppPage.signinButton(driver).click();
//			hockeyAppPage.qaConnectIcon(driver).click();
//			String InstallConnectMeLink = hockeyAppPage.installButton(driver).getAttribute("href");
//			Config.BuildNo = hockeyAppPage.appVersion(driver).getText().substring(13, 16);
//			System.out.println("build no" + Config.BuildNo);
//			driver.installApp(InstallConnectMeLink);
        }

    }

    public void openDeepLink(AppiumDriver driverBrowser, AppiumDriver driverApp, String link) throws Exception {
        System.out.println("Opening deeplink: " + link);

        if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS"))) {
            driverApp.manage().timeouts().implicitlyWait(AppDriver.SMALL_TIMEOUT, TimeUnit.SECONDS);

//            ApplicationState appState = driverBrowser.queryAppState("com.apple.mobilesafari"); // DEBUG
            ApplicationState appState = driverApp.queryAppState("com.apple.mobilesafari");
            System.out.println("Safari app state: " + appState);
            if(appState.equals(ApplicationState.RUNNING_IN_BACKGROUND) || appState.equals(ApplicationState.RUNNING_IN_FOREGROUND)
                || appState.equals(ApplicationState.RUNNING_IN_BACKGROUND_SUSPENDED))
            {
                // Call to kill safari only when it's already running, otherwise it'll terminate tcp connection to the device
//                driverBrowser.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", "com.apple.mobilesafari")); // DEBUG
                driverApp.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", "com.apple.mobilesafari"));
            }

            List args = new ArrayList();
            args.add("-U");
            args.add(link);

            Map<String, Object> params = new HashMap<>();
            params.put("bundleId", "com.apple.mobilesafari");
            params.put("arguments", args);

//            driverBrowser.executeScript("mobile: launchApp", params); // DEBUG
            driverApp.executeScript("mobile: launchApp", params);
            driverApp.launchApp();
        } else {
            driverBrowser.get(link);
        }

        Thread.sleep(2000); // DEBUG IOS

        System.out.println("Contexts 2 >>> " + driverApp.getContextHandles()); // DEBUG
        System.out.println("Contexts 2 >>> " + driverApp.getContext()); // DEBUG
        new AppUtils().authForAction();
        System.out.println("Contexts 3 >>> " + driverApp.getContextHandles()); // DEBUG
        System.out.println("Contexts 3 >>> " + driverApp.getContext()); // DEBUG
    }

    public void getConnectionInvitation(AppiumDriver driverBrowser, AppiumDriver driverApp, String label, String invitationType) throws Exception {
        VASApi VAS = VASApi.getInstance();
        JSONObject relationship;

        try {
            relationship = VAS.createRelationship(label);
        } catch (Exception ex) {
            System.err.println(ex.toString());
            relationship = VAS.createRelationship(label);
        }

        LocalContext context = LocalContext.getInstance();
        context.setValue("DID", relationship.getString("DID"));

        JSONObject invitation = VAS.createConnectionInvitation(
            relationship.getString("relationshipThreadID"),
            relationship.getString("DID"),
            invitationType
        );

//		VAS.createIssuer(relationship.getString("relationshipThreadID"));
//		VAS.getIdentifier(relationship.getString("relationshipThreadID"));

        String inviteURL = invitation.getString("inviteURL");
        context.setValue(invitationType, Config.ConnectMe_App_Link + inviteURL); // save link for redirection cases

        openDeepLink(driverBrowser, driverApp, Config.ConnectMe_App_Link + inviteURL);
    }

    public void getConnectionInvitationFromAcaPyApi(AppiumDriver driverBrowser, AppiumDriver driverApp, String label) throws Exception {
        AcaPyApi ACAPY = AcaPyApi.getInstance();
        LocalContext context = LocalContext.getInstance();

        JSONObject invite = ACAPY.createConnectionInvitation();
        String inviteURL = invite.getString("invitation_url");
        String connectionId = invite.getString("connection_id");

        context.setValue("connectionId", connectionId);

        openDeepLink(driverBrowser, driverApp, Config.ConnectMe_App_Link + inviteURL);

        driverApp.context("NATIVE_APP");
    }

    public static String getInvitationLink(int index) {
        RestAssured.baseURI = Config.VAS_Server_Link;

        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .request(Method.GET);

        String responseBody = response.getBody().asString();
        System.out.println("GET Response Body is =>  " + responseBody);

        JSONArray result = new JSONArray();
        try {
            result = new JSONArray(responseBody);
        } catch (JSONException ex) {
            // ignore
        }
        System.out.println("String is =>  " + result.getString(index));

        return result.getString(index);
    }

    public static String ensureGetInvitationLink(int index) throws InterruptedException {
        try {
            return getInvitationLink(index);
        } catch (JSONException e) {
            try {
                Thread.sleep(120000);
                return getInvitationLink(index);
            } catch (JSONException ex) {
                Thread.sleep(180000);
                return getInvitationLink(index);
            }
        }
    }

    public void acceptPushNotificationRequest(AppiumDriver driverApp) {
        if (Config.iOS_Devices.contains(Config.Device_Type)) {
//            System.out.println("Contexts 4 >>> " + driverApp.getContextHandles()); // DEBUG
//            System.out.println("Contexts 4 >>> " + driverApp.getContext()); // DEBUG
//            if(!AppUtils.isElementAbsent(driverApp, pushNotificationsPageNew.allowButton))
//            {
                try {
                    System.out.println("Contexts 7 >>> " + driverApp.getContextHandles());
                    System.out.println("Contexts 7 >>> " + driverApp.getContext()); // DEBUG
                    pushNotificationsPageNew.allowButton.click();
                    pushNotificationsPageNew.okButton.click();
                } catch (Exception e) {
                    System.out.println("Contexts 8 >>> " + driverApp.getContextHandles());
                    System.out.println("Contexts 8 >>> " + driverApp.getContext()); // DEBUG
                }
//            }
//            else {
//                System.out.println("Contexts 8 >>> " + driverApp.getContextHandles());
//                System.out.println("Contexts 8 >>> " + driverApp.getContext()); // DEBUG
//                System.out.println("Permissions already have been granted!");
//            }
        }
    }

    // iOS simulator issue. Push notification doesn't work on iOS simulators. It causes unexpected modal view
    private void skipSimulatorPushNotificationsIssue(AppiumDriver driverApp) {
        if (Config.Device_Type.equals("iOSSimulator")) {
            try {
                pushNotificationsPageNew.notNowButton.click();
            } catch (Exception ex) {
                System.out.println("Permissions already have been granted (Not Now)!");
            }
        }
    }

    public void acceptConnectionInvitation(AppiumDriver driverApp) throws Exception {
        driverApp.context("NATIVE_APP"); // keep CM in foreground

//		skipSimulatorPushNotificationsIssue(driverApp);

        invitationPageNew.title.isDisplayed();
        invitationPageNew.inviteeAvatar.isDisplayed();
        invitationPageNew.inviteeAvatar.isDisplayed();
        invitationPageNew.denyButton.isDisplayed();
        invitationPageNew.connectButton.isDisplayed();

        invitationPageNew.connectButton.click();
        homePageNew.recentEventsSection.isDisplayed();
//		homePage.makingConnectionEvent(driverApp).isDisplayed(); FIXME: intermittent failure
    }

    public void rejectConnectionInvitation(AppiumDriver driverApp) throws Exception {
//		skipSimulatorPushNotificationsIssue(driverApp);

        invitationPageNew.title.isDisplayed();
        invitationPageNew.inviteeAvatar.isDisplayed();
        invitationPageNew.inviteeAvatar.isDisplayed();
        invitationPageNew.denyButton.isDisplayed();
        invitationPageNew.connectButton.isDisplayed();

        invitationPageNew.denyButton.click();
    }

    public void openConnectionHistory(String connectionName) throws Exception {
        homePageNew.tapOnBurgerMenu();
        menuPageNew.myConnectionsButton.click();
        myConnectionsPageNew.getConnectionByName(connectionName).isDisplayed();
        myConnectionsPageNew.getConnectionByName(connectionName).click();

        if(!AppUtils.isElementAbsent(driverApp, myConnectionsPageNew.getConnectionByName(connectionName)))
        {
            try {
                myConnectionsPageNew.getConnectionByName(connectionName).click();
            } catch (Exception e) {
                if(!AppUtils.isElementAbsent(driverApp, myConnectionsPageNew.getConnectionByName(connectionName)))
                {
                    try {
                        myConnectionsPageNew.getConnectionByName(connectionName).click();
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }
}

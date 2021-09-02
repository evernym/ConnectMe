package test.java.funcModules;

import com.google.common.collect.ImmutableMap;
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
import test.java.utility.IntSetup;
import test.java.appModules.VASApi;
import test.java.utility.Config;
import test.java.utility.AppDriver;
import test.java.utility.LocalContext;

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
			if (! link.equals("")) {
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

	public void openDeepLink(AppiumDriver driverBrowser, AppiumDriver driverApp, String link) throws InterruptedException {
		if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS"))) {
			driverApp.manage().timeouts().implicitlyWait(AppDriver.SMALL_TIMEOUT, TimeUnit.SECONDS);

			driverBrowser.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", "com.apple.mobilesafari"));

			List args = new ArrayList();
			args.add("-U");
			args.add(link);

			Map<String, Object> params = new HashMap<>();
			params.put("bundleId", "com.apple.mobilesafari");
			params.put("arguments", args);

			driverBrowser.executeScript("mobile: launchApp", params);
			driverApp.launchApp();
		} else {
			driverBrowser.get(link);
		}

		try {
			passCodePageNew.passCodeTitle.isDisplayed();
			passCodePageNew.enterPassCode();
		} catch (Exception e) {
			try {
        passCodePageNew.passCodeTitle.isDisplayed();
        passCodePageNew.enterPassCode();
			} catch (Exception ex) {
				System.out.println("Pincode is not needed here!");
			}
		} finally {
			driverApp.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
		}
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
        Thread.sleep(60000);
        return getInvitationLink(index);
      } catch (JSONException ex) {
        Thread.sleep(120000);
        return getInvitationLink(index);
      }
    }
  }

	public void acceptPushNotificationRequest(AppiumDriver driverApp) {
		if (Config.iOS_Devices.contains(Config.Device_Type)) {
			try {
				driverApp.manage().timeouts().implicitlyWait(AppDriver.SUPER_SMALL_TIMEOUT, TimeUnit.SECONDS);
				pushNotificationsPageNew.allowButton.click();
        pushNotificationsPageNew.okButton.click();
			} catch (Exception e) {
				System.out.println("Permissions already have been granted!");
			} finally {
				driverApp.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
			}
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
		homePageNew.burgerMenuButton.click();
		menuPageNew.myConnectionsButton.click();
		Thread.sleep(1000);
    myConnectionsPageNew.testConnection(connectionName).click();
    // FIXME - validate that we drilled down to connection properly
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      try {
        myConnectionsPageNew.testConnection(connectionName).click();
      } catch (Exception e) {

      }
    }
	}
}

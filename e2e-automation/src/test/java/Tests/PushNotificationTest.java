package test.java.Tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.appium.java_client.android.AndroidDriver;
import org.json.JSONObject;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.*;
import appModules.AppInjector;
import test.java.appModules.AppUtils;
import test.java.appModules.VASApi;
import test.java.utility.IntSetup;
import test.java.funcModules.ConnectionModules;
import test.java.utility.Helpers;
import test.java.utility.LocalContext;
import test.java.utility.AppDriver;
import test.java.utility.BrowserDriver;
import test.java.pageObjects.HomePage;
import test.java.pageObjects.MenuPage;
import test.java.pageObjects.CredentialPage;
import test.java.utility.Constants;
import test.java.utility.Config;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PushNotificationTest extends IntSetup {
  Injector injector = Guice.createInjector(new AppInjector());
  private AppUtils objAppUtlis = injector.getInstance(AppUtils.class);
  private ConnectionModules objConnectionModules = injector.getInstance(ConnectionModules.class);
  private HomePage homePage = injector.getInstance(HomePage.class);
  private MenuPage menuPage = injector.getInstance(MenuPage.class);
  private CredentialPage credentialPage = injector.getInstance(CredentialPage.class);
  private test.java.pageObjects.QuestionPage questionPage = injector.getInstance(test.java.pageObjects.QuestionPage.class);

  private VASApi VAS = VASApi.getInstance();
  private LocalContext context = LocalContext.getInstance();

  String connectionName = Helpers.randomString();
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
    driverApp = AppDriver.getDriver();
    driverBrowser = BrowserDriver.getDriver();

    if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS"))) return;

    AppUtils.DoSomethingEventually(
      () -> objConnectionModules.getConnectionInvitation(driverBrowser, driverApp, connectionName, invitationType),
      () -> objConnectionModules.acceptConnectionInvitation(driverApp)
    );
    context.setValue("connectionName", connectionName);

    AppUtils.waitForElement(driverApp, () -> homePage.connectedEvent(driverApp, connectionName)).isDisplayed();
  }


  @Test(dataProvider = "appStates")
  public void checkCredOfferNotificationAppRunningInBackground(String appState) throws Exception {
    if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS"))) return;

    String credentialName = Helpers.randomString();

    switch (appState) {
      case appBackgroundLocked:
        driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
        try {
          ((AndroidDriver) driverApp).lockDevice();
        } catch (WebDriverException e) {
          // TODO: this bug has been fixed in 6.0+ client version
          if (!((AndroidDriver) driverApp).isLocked()) ((AndroidDriver) driverApp).lockDevice();
        }
        break;
      case appBackground:
        driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
        break;
    }

    VAS.sendCredentialOffer(context.getValue("DID"), "PMzJsfuq4YYPAKHLSrdP4Q:3:CL:185320:tag", Constants.values, credentialName);

    if (((AndroidDriver) driverApp).isLocked()) ((AndroidDriver) driverApp).unlockDevice();

    ((AndroidDriver) driverApp).openNotifications();

    AppUtils.DoSomethingEventually(
      () -> homePage.credentialOfferNotification(driverApp).click()
    );

    driverApp.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    try {
      objAppUtlis.unlockApp(driverApp);
    } catch (Exception ex) {
      System.out.println("Unlocking is not needed here!");
    }

    try {
      homePage.newMessage(driverApp).click();
    } catch (Exception ex) {
      System.out.println("New message tapping is not needed here!");
    }
    driverApp.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    objAppUtlis.acceptCredential(driverApp);
    homePage.recentEventsSection(driverApp).isDisplayed();
    AppUtils.waitForElement(driverApp, () -> homePage.credentialIssuedEvent(driverApp, credentialName)).isDisplayed();
  }

  @Test(dataProvider = "appStates")
  public void checkProofRequestNotificationAppRunningInBackground(String appState) throws Exception {
    if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS"))) return;

    switch (appState) {
      case appBackgroundLocked:
        driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
        try {
          ((AndroidDriver) driverApp).lockDevice();
        } catch (WebDriverException e) {
          // TODO: this bug has been fixed in 6.0+ client version
          if (!((AndroidDriver) driverApp).isLocked()) ((AndroidDriver) driverApp).lockDevice();
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

    if (((AndroidDriver) driverApp).isLocked()) ((AndroidDriver) driverApp).unlockDevice();

    ((AndroidDriver) driverApp).openNotifications();

    AppUtils.DoSomethingEventually(
      () -> homePage.proofRequestNotification(driverApp).click()
    );

    driverApp.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    try {
      objAppUtlis.unlockApp(driverApp);
    } catch (Exception ex) {
      System.out.println("Unlocking is not needed here!");
    }

    try {
      homePage.newMessage(driverApp).click();
    } catch (Exception ex) {
      System.out.println("New message tapping is not needed here!");
    }
    driverApp.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    objAppUtlis.shareProof(driverApp);
    homePage.recentEventsSection(driverApp).isDisplayed();
    AppUtils.waitForElement(driverApp, () -> homePage.proofSharedEvent(driverApp, proofName)).isDisplayed();
  }

  @Test(dataProvider = "appStates")
  public void checkStructuredMessageNotificationAppRunningInBackground(String appState) throws Exception {
    if ((Config.Device_Type.equals("iOS") || Config.Device_Type.equals("awsiOS"))) return;

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
          if (!((AndroidDriver) driverApp).isLocked()) ((AndroidDriver) driverApp).lockDevice();
        }
        break;
      case appBackground:
        driverBrowser.runAppInBackground(Duration.ofSeconds(-1));
        break;
    }

    VAS.askQuestion(DID, text, detail, option);

    if (((AndroidDriver) driverApp).isLocked()) ((AndroidDriver) driverApp).unlockDevice();

    ((AndroidDriver) driverApp).openNotifications();

    AppUtils.DoSomethingEventually(
      () -> homePage.questionNotification(driverApp).click()
    );

    driverApp.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    try {
      objAppUtlis.unlockApp(driverApp);
    } catch (Exception ex) {
      System.out.println("Unlocking is not needed here!");
    }

    try {
      homePage.newMessage(driverApp).click();
    } catch (Exception ex) {
      System.out.println("New message tapping is not needed here!");
    }
    driverApp.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    AppUtils.waitForElement(driverApp, () -> questionPage.header(driverApp)).isDisplayed();
    questionPage.senderLogo(driverApp).isDisplayed();
    questionPage.senderName(driverApp, context.getValue("connectionName")).isDisplayed();
    questionPage.title(driverApp, text).isDisplayed();
    questionPage.description(driverApp, detail).isDisplayed();
  }

  @AfterClass
  public void AfterClass() {
    BrowserDriver.closeApp();
    driverApp.closeApp();
    System.out.println("Push Notification Test Suite has been finished!");
  }
}

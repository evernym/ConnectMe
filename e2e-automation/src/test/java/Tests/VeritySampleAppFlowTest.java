package test.java.Tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;
import test.java.utility.BrowserDriver;
import test.java.utility.Config;
import test.java.funcModules.ConnectionModules;
import test.java.appModules.AppUtils;


public class VeritySampleAppFlowTest extends IntSetup {
  private AppUtils AppUtilsInstance = new AppUtils();

  @BeforeClass
  public void classSetup() {
    System.out.println("Before class setup!");
    try {
      startUpPageNew.setUpButton.click();
      for (int i = 0; i < 2; i++) {
        passCodePageNew.enterPassCode();
      }
      startUpPageNew.switchEnv();
    } catch (Exception e) {
      System.exit(1); // don't run main test if this fails
    }
  }

  @Test
  public void verityFlowTest() throws Exception {
    // establish connection
    driverBrowser = BrowserDriver.getDriver();

    for (int i = 0; i < 3; i++) {
      driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.getInvitationLink());
      passCodePageNew.passCodeTitle.isDisplayed();
      passCodePageNew.enterPassCode();
      invitationPageNew.title.isDisplayed();
      invitationPageNew.connectButton.click();
      Thread.sleep(15000);
    }

    // answer question
    AppUtils.waitForElementNew(driverApp, questionPageNew.header);
    String answer = "Great!";
    AppUtilsInstance.findParameterizedElement(answer).click();

    // accept credential
    AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader);
    String credentialName = "Diploma"; // credential shows schema name now
    AppUtilsInstance.acceptCredential();

    // share proof
    AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);
    String proofName = "Proof of Degree";
    AppUtilsInstance.shareProof();

    // check all events
    homePageNew.questionRespondedEvent(answer).isDisplayed();
    AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(credentialName));
    AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
  }

  @AfterClass
  public void classTeardown() throws Exception {
    System.out.println("After class teardown!");
    driverApp.closeApp();
  }
}

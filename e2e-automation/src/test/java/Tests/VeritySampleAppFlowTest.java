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
  private static final int connection_number = 5;

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
    for (int i = 0; i < connection_number; i++) {
      // establish connection
      driverBrowser = BrowserDriver.getDriver();

      if (i == 4) { // ci reuse case
        driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.getInvitationLink(i - 1));
      } else {
        driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.getInvitationLink(i));
      }
      passCodePageNew.passCodeTitle.isDisplayed();
      passCodePageNew.enterPassCode();
      if (i == 1) { // oob reuse case
        homePageNew.homeHeader.isDisplayed();
      } else {
        invitationPageNew.title.isDisplayed();
        invitationPageNew.connectButton.click();
      }

      Thread.sleep(60000); // FIXME: establishing connection on CM side

      BrowserDriver.closeApp();
      driverApp.closeApp();
    }

    passCodePageNew.openApp();

    String[] answers = new String[] { "Ok!", "Great!", "Awful", "Nice" };
    for (String answer: answers) {
      // answer question
      try {
        homePageNew.newMessage.click();
      } catch (Exception e) {
        AppUtils.waitForElementNew(driverApp, questionPageNew.header);
      }
      if (answer.equals("Awful") || answer.equals("Nice")) {
        questionPageNew.answerOption(answer).click();
        questionPageNew.submitButton.click();
      } else {
        AppUtilsInstance.findParameterizedElement(answer).click();
      }
    }

    // accept credential
    AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader);
    String credentialName = "Diploma"; // credential shows schema name now
    AppUtilsInstance.acceptCredential();

    // share proof
    AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);
    String proofName = "Proof of Degree";
    AppUtilsInstance.shareProof();

    // check all events
    for (String answer: answers) {
      homePageNew.questionRespondedEvent(answer).isDisplayed();
    }
    AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(credentialName));
    AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(proofName));
  }

  @AfterClass
  public void classTeardown() throws Exception {
    System.out.println("After class teardown!");
    driverApp.closeApp();
  }
}

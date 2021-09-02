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
  private static final int connection_cases = 5; // 5
  private static final int oob_attachment_cases = 2;
  private static final int step_wait = 15000; // tune this to fix intermittent failures

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
    for (int i = 0; i < connection_cases; i++) {
      // establish connection
      driverBrowser = BrowserDriver.getDriver();

      if (i == 4) { // ci reuse case
        driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(i - 2));
      } else {
        driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(i));
      }
      passCodePageNew.passCodeTitle.isDisplayed();
      passCodePageNew.enterPassCode();
      try {
        invitationPageNew.title.isDisplayed();
        invitationPageNew.connectButton.click();
        try {
          invitationPageNew.connectButton.click();
        } catch (Exception ignored) {

        }
        System.out.println("Connection #" + (i+1) + " was established!");
      } catch (Exception e) { // reuse cases
        homePageNew.homeHeader.isDisplayed();
      }

      Thread.sleep(30000); // FIXME: establishing connection on CM side

      BrowserDriver.closeApp();
      driverApp.closeApp();
    }

    passCodePageNew.openApp();

    // sometimes connection appears after app restart
    try {
      invitationPageNew.title.isDisplayed();
      invitationPageNew.connectButton.click();
    } catch (Exception ignored) {

    }

    String[] answers = new String[] { "Ok!", "Great!", "Awful", "Nice", "Yep" };
//    String[] answers = new String[] { "Ok!" };
    for (String answer: answers) {
      // answer question
      try {
        homePageNew.newMessage.click();
      } catch (Exception e) {
        AppUtils.waitForElementNew(driverApp, questionPageNew.header);
      }
      if (answer.equals("Ok!") || answer.equals("Great!")) { // action buttons
        AppUtilsInstance.findParameterizedElement(answer).click();
      } else { // radio buttons
        questionPageNew.answerOption(answer).click();
        questionPageNew.submitButton.click();
      }

      Thread.sleep(step_wait);
    }

    String[][] creds_and_proofs = new String[][] {
      {"Passport", "Proof of Health"},
      {"Diploma", "Proof of Degree"},
      {"Schema #1", "Proof #1"},
      {"Schema #2", "Proof #2"},
      {"Attachment Schema", "Proof of Attachments"}
    };
//    String[][] creds_and_proofs = new String[][] {
//      {"Passport", "Proof of Health"}
//    };
    for (String[] entry: creds_and_proofs) {
      // accept credential
      AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader);
      AppUtilsInstance.acceptCredential();
      Thread.sleep(step_wait);

      // share proof
      AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);
      AppUtilsInstance.shareProof();
      Thread.sleep(step_wait);
    }

    for (int i = 0; i < oob_attachment_cases; i++) {
      BrowserDriver.closeApp();
      driverApp.closeApp();
      // oob attachment case #1
      driverBrowser = BrowserDriver.getDriver();
      driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(connection_cases + i));
      passCodePageNew.passCodeTitle.isDisplayed();
      passCodePageNew.enterPassCode();
      // accept credential or share proof
      try {
        if (i == 0) {
          credentialPageNew.credentialOfferHeader.isDisplayed();
          AppUtilsInstance.acceptCredential();
        } else {
          proofRequestPageNew.proofRequestHeader.isDisplayed();
          AppUtilsInstance.shareProof();
        }
      } catch (Exception e) {
        BrowserDriver.closeApp();
        driverApp.closeApp();
        driverBrowser = BrowserDriver.getDriver();
        driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(connection_cases + i));
        passCodePageNew.passCodeTitle.isDisplayed();
        passCodePageNew.enterPassCode();
        if (i == 0) {
          credentialPageNew.credentialOfferHeader.isDisplayed();
          AppUtilsInstance.acceptCredential();
        } else {
          proofRequestPageNew.proofRequestHeader.isDisplayed();
          AppUtilsInstance.shareProof();
        }
      }
      Thread.sleep(step_wait);
    }
  }

  @AfterClass
  public void classTeardown() throws Exception {
    System.out.println("After class teardown!");
    driverApp.closeApp();
  }
}

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
  private static final int connection_number = 1; // 5

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

      Thread.sleep(60000); // FIXME: establishing connection on CM side

      BrowserDriver.closeApp();
      driverApp.closeApp();
    }

    passCodePageNew.openApp();

    String[] answers = new String[] { "Ok!", "Great!", "Awful", "Nice", "Yep" };
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

      Thread.sleep(15000);
    }

    String[][] creds_and_proofs = new String[][] {
      {"Passport", "Proof of Health"},
      {"Diploma", "Proof of Degree"},
      {"Schema #1", "Proof #1"},
      {"Schema #2", "Proof #2"},
      {"Attachment Schema", "Proof of Attachments"}
    };
    for (String[] entry: creds_and_proofs) {
      // accept credential
      AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader);
      AppUtilsInstance.acceptCredential();
      Thread.sleep(15000);

      // share proof
      AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);
      AppUtilsInstance.shareProof();
      Thread.sleep(15000);
    }

    BrowserDriver.closeApp();
    driverApp.closeApp();

    // oob attachment case #1
    driverBrowser = BrowserDriver.getDriver();
    driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(connection_number));
    // accept credential
    AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader);
    AppUtilsInstance.acceptCredential();
    Thread.sleep(15000);

    BrowserDriver.closeApp();
    driverApp.closeApp();

    // oob attachment case #2
    driverBrowser = BrowserDriver.getDriver();
    driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(connection_number + 1));
    // share proof
    AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);
    AppUtilsInstance.shareProof();
    Thread.sleep(15000);

//    // check all events
//    // TODO: swipe is needed due to many events on Home!
//    for (String answer: answers) {
//      homePageNew.questionRespondedEvent(answer).isDisplayed();
//    }

//    for (String[] entry: creds_and_proofs) {
//      AppUtils.waitForElementNew(driverApp, homePageNew.credentialIssuedEvent(entry[0]));
//      AppUtils.waitForElementNew(driverApp, homePageNew.proofSharedEvent(entry[1]));
//    }
  }

  @AfterClass
  public void classTeardown() throws Exception {
    System.out.println("After class teardown!");
    driverApp.closeApp();
  }
}

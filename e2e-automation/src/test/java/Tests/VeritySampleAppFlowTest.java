package test.java.Tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.java.utility.IntSetup;
import test.java.utility.BrowserDriver;
import test.java.utility.Config;
import test.java.funcModules.ConnectionModules;
import test.java.appModules.AppUtils;
import test.java.utility.Helpers;


public class VeritySampleAppFlowTest extends IntSetup {
    private AppUtils AppUtilsInstance = new AppUtils();
    private static final int connection_cases = 5; // 5
    private static final int oob_attachment_cases = 2;
    private static final int step_wait = 30000; // tune this to fix intermittent failures

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
                System.out.println("Connection #" + (i + 1) + " was established!");
                Thread.sleep(step_wait * 2); // FIXME: establishing connection on CM side
            } catch (Exception e) { // CM-3035
                BrowserDriver.closeApp();
                driverApp.closeApp();
                passCodePageNew.openApp();
                try {
                    invitationPageNew.title.isDisplayed();
                    invitationPageNew.connectButton.click();
                    System.out.println("Connection #" + (i + 1) + " was established after app restart!");
                    Thread.sleep(step_wait * 2); // FIXME: establishing connection on CM side
                } catch (Exception ex) { // reuse cases
//          homePageNew.homeHeader.isDisplayed();
                }
            }

            BrowserDriver.closeApp();
            driverApp.closeApp();
        }

        passCodePageNew.openApp();
        // this is needed if you run 1 connection test
//    try { // CM bug!
//      invitationPageNew.connectButton.click();
//    } catch (Exception ignored) {
//
//    }

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
            {"Passport", "Proof of Age"},
            {"Diploma", "Proof of Degree"},
            {"Schema #1", "Proof #1"},
            {"Schema #2", "Proof #2"},
            {"Attachment Schema", "Proof of Attachments"}
        };
//    String[][] creds_and_proofs = new String[][] {
//      {"Passport", "Proof of Age"},
//      {"Diploma", "Proof of Degree"}
//    };
        for (String[] entry: creds_and_proofs) {
            // accept credential
//            AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader);

            try {
                AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader, 10);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                AppUtils.DoSomethingEventuallyNew(15,
                    () -> driverApp.terminateApp("me.connect"),
                    () -> driverApp.launchApp(),
                    () -> new AppUtils().authForAction(),
                    () -> AppUtils.waitForElementNew(driverApp, credentialPageNew.credentialOfferHeader, 10));
            }

            AppUtilsInstance.acceptCredential();
            Thread.sleep(step_wait);

            // share proof
//            AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

            try {
                AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader, 10);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                AppUtils.DoSomethingEventuallyNew(15,
                    () -> driverApp.terminateApp("me.connect"),
                    () -> driverApp.launchApp(),
                    () -> new AppUtils().authForAction(),
                    () -> AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader, 10));
            }

            AppUtilsInstance.shareProof();
            Thread.sleep(step_wait);
        }

        String[] self_attested = new String[] { "unknown1", "unknown2", "unknown3"};
        for (String entry: self_attested) {
            // self-attested proof requests
            AppUtils.waitForElementNew(driverApp, proofRequestPageNew.proofRequestHeader);

            proofRequestPageNew.missingAttributePlaceholder.click();
            customValuesPageNew.title.isDisplayed();
            customValuesPageNew.attributeNameLabel(entry).click();
            customValuesPageNew.customValueInput.sendKeys(Helpers.randomString());
            AndroidDriver androidDriver = (AndroidDriver) driverApp;
            androidDriver.pressKey(new KeyEvent(AndroidKey.ENTER));

            AppUtilsInstance.shareProof();
            Thread.sleep(step_wait);
        }

        for (int i = 0; i < oob_attachment_cases; i++) {
            BrowserDriver.closeApp();
            driverApp.closeApp();
            // oob attachment case #1
            driverBrowser = BrowserDriver.getDriver();
            driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink((connection_cases - 1) + i)); // for 5!
//      driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(connection_cases + i)); // for 1!
            driverApp.context("NATIVE_APP");
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
                driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink((connection_cases - 1) + i)); // for 5!
//        driverBrowser.get(Config.ConnectMe_App_Link + ConnectionModules.ensureGetInvitationLink(connection_cases + i)); // for 1!
                driverApp.context("NATIVE_APP");
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
            Thread.sleep(step_wait * 2);
        }
    }

    @AfterClass
    public void classTeardown() throws Exception {
        System.out.println("After class teardown!");
        driverApp.closeApp();
    }
}
